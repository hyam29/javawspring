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
			@RequestParam(name="pageSize", defaultValue = "1", required = false) int pageSize,
			@RequestParam(name="part", defaultValue = "", required = false) String part,
			@RequestParam(name="searchString", defaultValue = "", required = false) String searchString) {
		
		// 페이징 처리 (매개변수에 null로 두지 말기! -> "" (nullPointException 방지))
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "board", "part", "searchString");
		List<BoardVO> vos = boardService.getBoardList(pageVo.getStartIdxNo(), pageSize, part, searchString);
		
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("vos", vos);
		
		return "board/boardList";
	}
	
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
	
	@RequestMapping(value="/boardInput", method=RequestMethod.POST)
	public String boardInputPost(BoardVO vo) {
		int res = boardService.setBoardInput(vo);
		
		if(res == 1) return "redirect:/msg/boardInputOk";
		else return "redirect:/msg/boardInputNo";
	}
	
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
		
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pag", pag);
		model.addAttribute("vo", vo);
		model.addAttribute("flag", flag);
		model.addAttribute("search", search);
		model.addAttribute("searchString", searchString);
		*/
		
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
		
		BoardVO vo = boardService.getBoardContent(idx);
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
		return "board/boardContent";
	}
	
	/* 좋아요 처리 ajax */
	@ResponseBody
	@RequestMapping(value="/boardGood", method = RequestMethod.POST)
	public String boardGoodPost(int idx) {
		int res = boardService.setBoardGood(idx);
		return res+"";
	}

	
}
