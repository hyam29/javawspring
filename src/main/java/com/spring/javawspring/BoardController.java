package com.spring.javawspring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.BoardService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	BoardService boardService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PageProcess pageProcess;
	
	/* 게시판 리스트 */
	@RequestMapping(value="/boardList", method=RequestMethod.GET)
	public String boardListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(name="search", defaultValue = "", required = false) String search,
			@RequestParam(name="searchString", defaultValue = "", required = false) String searchString) {
		
		// 페이징 처리 (매개변수에 null로 두지 말기! -> "" (nullPointException 방지))
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "board", search, searchString);
		List<BoardVO> vos = boardService.getBoardList(pageVo.getStartIdxNo(), pageSize, search, searchString);
		
		//PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "board", "", "");
		//List<BoardVO> vos = boardService.getBoardList(pageVo.getStartIdxNo(), pageSize);
		
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("vos", vos);
		model.addAttribute("search", search);
		model.addAttribute("searchString", searchString);
		
		return "board/boardList";
	}
	
	/* 게시판 글 작성 폼 */
	@RequestMapping(value="/boardInput", method=RequestMethod.GET)
	public String boardInputGet(Model model, HttpSession session, int pag, int pageSize) {
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		model.addAttribute("email", vo.getEmail());
		model.addAttribute("homePage", vo.getHomePage());
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		return "board/boardInput";
	}
	
	/* 게시판 글 작성 처리 */
	@RequestMapping(value="/boardInput", method=RequestMethod.POST)
	public String boardInputPost(BoardVO vo) {
		
		// content에 이미지가 저장되어 있다면, 저장된 이미지만 골라서 /resources/data/board/폴더에 저장시켜 준다.
		boardService.imgCheck(vo.getContent());
		
		// 이미지 복사작업이 끝나면, board폴더에 실제로 저장된 파일명을 DB에 저장! (/resources/data/ckeditor/ -> /resources/data/board/ 폴더 변경)
		vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/ckeditor/board/"));
		
		int res = boardService.setBoardInput(vo);
		
		if(res == 1) return "redirect:/msg/boardInputOk";
		else return "redirect:/msg/boardInputNo";
	}
	
	/* 게시판 글 보기 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/boardContent", method=RequestMethod.GET)
	public String boardContentGet(Model model, HttpSession session, int idx, int pag, int pageSize) {
		// 조회수 증가 처리
		// boardService.setBoardReadNum(idx);
		
		// 글 조회수 1회만 증가시키기.(조회수 중복방지처리 - 세션 사용 : 'board+고유번호'를 객체배열에 추가시킨다.)
		ArrayList<String> contentIdx = (ArrayList<String>) session.getAttribute("sContentIdx");
		if(contentIdx == null) {
			contentIdx = new ArrayList<>();
		}
		String imsiContentIdx = "board" + idx;
		if(!contentIdx.contains(imsiContentIdx)) {
			boardService.setBoardReadNum(idx);
			contentIdx.add(imsiContentIdx);
		}
		session.setAttribute("sContentIdx", contentIdx);
		
		/*
		BoardVO vo = boardService.getBoardContentSearch(idx);
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "board", "part", "");
		model.addAttribute("flag", flag);
		
		*/
		
		/* 게시글 vo에 담기 */
		BoardVO vo = boardService.getBoardContent(idx);
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
  	// 해당글에 좋아요 버튼을 클릭하였었다면 '좋아요세션'에 아이디를 저장시켜두었기에 찾아서 있다면 sSw값을 1로 보내어 하트색을 빨강색으로 변경유지하게한다.
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "boardGood" + idx;
		if(goodIdx.contains(imsiGoodIdx)) {
			session.setAttribute("sSw", "1");		// 로그인 사용자가 이미 좋아요를 클릭한 게시글이라면 빨강색으로 표시가히위해 sSW에 1을 전송하고 있다.
		}
		else {
			session.setAttribute("sSw", "0");
		}
		
		/* DB에서 현재 게시글의 '좋아요' 체크여부 */
		String mid = (String) session.getAttribute("sMid");
		//GoodVO goodVo = boardService.getBoardGoodCheck(idx, "board", mid);
		
		//model.addAttribute("goodVo", goodVo);
		
		/* 이전글/다음글 가져오기 */
		ArrayList<BoardVO> pnVos = boardService.getPrevNext(idx);
		
		//System.out.println("pnVos : " + pnVos);
		
		model.addAttribute("pnVos", pnVos);
		
		return "board/boardContent";
	}
	
	/* 좋아요 처리 ajax */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value="/boardGood", method = RequestMethod.POST)
	public String boardGoodPost(HttpSession session, int idx) {
		// int res = boardService.setBoardGood(idx);
		
		String res = "0"; // 처음 0으로 셋팅하고, 처음 좋아요 버튼을 누르면 '1'을 돌려준다., 이미 '좋아요'를 한번 눌렀으면 '0'으로 res값을 보내준다.
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "boardGood" + idx;
		if(!goodIdx.contains(imsiGoodIdx)) {
			boardService.setBoardGood(idx);
			goodIdx.add(imsiGoodIdx);
			res = "1";	// 처음으로 좋아요 버튼을 클릭하였기에 '1'을 반환
		}
		session.setAttribute("sGoodIdx", goodIdx);
		
		return res;
	}
	
	/* 따봉 처리 (따봉버튼 누르면 +1처리 (역따봉버튼 누르면 -1) */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/boardGoodPlusMinus", method = RequestMethod.POST)
	public String boardGoodGet(HttpSession session, int idx, int goodCnt) {
		String res = "0";
		// 좋아요수 단 한번씩만 1회 증가/감소시키기.(중복방지처리 - 세션 사용 : 'good+(고유번호*goodCnt)'를 객체배열에 추가시킨다.)
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "good" + (idx*goodCnt);
		if(!goodIdx.contains(imsiGoodIdx)) {
			boardService.setGoodPlusMinus(idx, goodCnt);	// 좋아요수 증가/감소
			goodIdx.add(imsiGoodIdx);
		}
		else {
			res = "1";
		}
		session.setAttribute("sGoodIdx", goodIdx);
		return res;
	}
	
	/* 게시판 글 삭제 처리 */
	@RequestMapping(value = "/boardDeleteOk", method = RequestMethod.GET)
	public String boardDeleteOkGet(Model model, int idx, int pag, int pageSize) {
		// 게시글에 사진이 존재한다면, 서버에 있는 사진 파일을 먼저 삭제 처리
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(vo.getContent());
		
		// DB에서 실제 존재하는 게시글 삭제 처리
		boardService.setBoardDeleteOk(idx);
		
		model.addAttribute("flag", "?pag="+pag+"&pageSize="+pageSize);
		
		return "redirect:/msg/boardDeleteOk";
	}
	
	/* 게시판 글 수정 폼 */
	@RequestMapping(value="/boardUpdate", method=RequestMethod.GET)
	public String boardUpdateGet(Model model, int idx, int pag, int pageSize) {
		// 수정창으로 이동 시, 먼저 원본파일에 그림파일이 있다면 현재폴더(board)의 그림파일을 ckeditor폴더로 복사시켜 줌!
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgCheckUpdate(vo.getContent());
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
		return "board/boardUpdate";
	}
	
	/* 게시판 글 수정 처리(그림 포함) */
	@RequestMapping(value="/boardUpdate", method=RequestMethod.POST)
	public String boardUpdatePost(Model model, BoardVO vo, int pag, int pageSize) {
		// 수정된 자료가 원본자료와 완전동일(찐찐동일)하다면, 수정할 필요가 없으므로 DB에 저장된 원본 자료를 불러와서 비교 처리
		BoardVO origVo = boardService.getBoardContent(vo.getIdx());
		
		// content 객체비교(DB저장된vo(origVo) 수정된VO(vo)) (객체비교도 .equals로 가능)
		if(!origVo.getContent().equals(vo.getContent())) {
			// 실제 수정하기 버튼 클릭 -> 기존의 board폴더에 저장된 현재 content의 그림파일 모두 삭제 처리(그림파일이 있을때만 수행) (다시 input의 개념으로 올릴거니까)
			if(origVo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(origVo.getContent());
				
			// vo.getContent() 들어있는 파일의 경로는 '/ckeditor/board/' 경로이므로 -> '/ckeditor/' 경로로 변경시켜줘야 함 (input 때 ckeditor에서 board로 복사하므로)
			vo.setContent(vo.getContent().replace("/data/ckeditor/board/", "/data/ckeditor/"));
			
			// 앞의 모든 준비가 끝나면, 파일을 처음 업로드한 것과 동일한 개념으로 작업 처리 (처음 게시글 올릴때 파일복사 작업과 동일한 작업)
			boardService.imgCheck(vo.getContent());
			
			// 파일 업로드 처리가 완료되면 다시 경로 수정 ('/ckeditor/' 경로이므로 -> '/ckeditor/board/') (즉, 변경된 vo.getContent -> vo.setContent() 처리)
			vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/ckeditor/board/"));
			
		}
		
		// 정비된 VO를 DB에 Update 시켜준다.
		boardService.setBoardUpdateOk(vo);
		
		model.addAttribute("flag", "?pag="+pag+"&pageSize="+pageSize);
		
		return "redirect:/msg/boardUpdateOk";
	}
	
	
	
}
