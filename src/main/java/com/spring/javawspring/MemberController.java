package com.spring.javawspring;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	PageProcess pageProcess;
	
	@RequestMapping(value = "/memberLogin", method=RequestMethod.GET)
	public String memberLoginGet(HttpServletRequest request) {
		// 로그인폼 호출 시, 기존에 저장된 쿠키가 있다면 불러와 mid에 담아 넘겨줌. (model에 담아도 됨)
		Cookie[] cookies = request.getCookies();
		for(int i=0; i<cookies.length; i++) {
			if(cookies[i].getName().equals("cMid")) {
				request.setAttribute("mid", cookies[i].getValue());
				break;
			}
		}
		return "member/memberLogin";
	}
	
	@RequestMapping(value = "/memberLogin", method=RequestMethod.POST)
	public String memberLoginPost(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid, 
			@RequestParam(name="pwd", defaultValue = "", required = false) String pwd, 
			@RequestParam(name="idCheck", defaultValue = "", required = false) String idCheck) {
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		// matches(가져온값, 비교할값) 위치중요!
		if(vo != null && passwordEncoder.matches(pwd, vo.getPwd()) && vo.getUserDel().equals("NO")) {
			/* 회원 인증처리된 경우 수행할 내용?
			  1. strLevel 처리
				2. session에 필요한 자료 저장
				3. 쿠키값 처리
				4. 그날(당일) 방문자 수 1증가 (방문포인트 증가)
			*/
			
			// 1~2. strLevel 처리, session에 필요한 자료 저장
			String strLevel = "";
			if(vo.getLevel() == 0) strLevel = "관리자";
			else if(vo.getLevel() == 1) strLevel = "운영자";
			else if(vo.getLevel() == 2) strLevel = "우수회원";
			else if(vo.getLevel() == 3) strLevel = "정회원";
			else if(vo.getLevel() == 4) strLevel = "준회원";
			
			session.setAttribute("sMid", mid);
			session.setAttribute("sLevel", vo.getLevel());
			session.setAttribute("sStrLevel", strLevel);
			session.setAttribute("sNickName", vo.getNickName());
			
			// 3. 쿠키값 처리 (service에서 해도 됨)
			if(idCheck.equals("on")) {
				Cookie cookie = new Cookie("cMid", mid);
				// 60초*1시간*하루*일주일
				cookie.setMaxAge(60*60*24*7);
				response.addCookie(cookie);
			}
			else {
				Cookie[] cookies = request.getCookies();
				for(int i=0; i<cookies.length; i++) {
					// getName() : 배열에 저장된 i번째 쿠키의 변수명을 불러와야 함 (getValue() XXX)
					if(cookies[i].getName().equals("cMid")) {
						cookies[i].setMaxAge(0);
						response.addCookie(cookies[i]);
						break;
					}
				}
			}
			// 4. 로그인한 회원의 당일 방문횟수 & 포인트 누적
			memberService.setMemberVisitProcess(vo);
			
			
			return "redirect:/msg/memberLoginOk?mid="+mid;
		}
		else {
			return "redirect:/msg/memberLoginNo";
		}
	}
	
	@RequestMapping(value="/memberLogout", method=RequestMethod.GET)
	public String memberLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		
		session.invalidate();
		
		return "redirect:/msg/memberLogout?mid="+mid;
	}

	@RequestMapping(value = "/memberMain", method=RequestMethod.GET)
	public String memberMainGet(Model model, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		model.addAttribute("vo", vo);
		
		return "member/memberMain";
	}
	
	@RequestMapping(value = "/adminLogout", method=RequestMethod.GET)
	public String adminLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sAMid");
		
		session.invalidate();
		
		return "redirect:/msg/memberLogout?mid="+mid;
	}
	
	/* 회원가입 폼 */
	@RequestMapping(value = "/memberJoin", method=RequestMethod.GET)
	public String memberJoinGet() {
		return "member/memberJoin";
	}
	
	/* 회원가입 처리 */
	@RequestMapping(value = "/memberJoin", method=RequestMethod.POST)
	public String memberJoinPost(MemberVO vo) {
		//System.out.println("memberVo : " + vo);
		
		// 동일 ID, 닉네임 백엔드 중복 체크 (중요한 부분만 백엔드에서 중복확인! 참고: 유효성검사는 VO에서 함)
		if(memberService.getMemberIdCheck(vo.getMid()) != null) {
			return "redirect:/msg/memberIdCheckNo";
		}
		if(memberService.getMemberNickNameCheck(vo.getNickName()) != null) {
			return "redirect:/msg/memberNickNameCheckNo";
		}
		
		// 비밀번호 암호화(BCryptPasswordEncoder)
		vo.setPwd(passwordEncoder.encode(vo.getPwd()));
		
		// 체크가 완료되면 vo에 담긴 자료를 DB에 저장! (회원가입)
		int res = memberService.setMemberJoinOk(vo);
		System.out.println("res : " + res);
		if(res == 1) return "redirect:/msg/memberJoinOk";
		else return "redirect:/msg/memberJoinNo";
	}
	
	// ID 중복확인 ajax 처리 -> @ResponseBody 필수!
	@ResponseBody
	@RequestMapping(value = "/memberIdCheck", method=RequestMethod.POST)
	public String memberIdCheckGet(String mid) {
		String res = "0";
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo != null) res = "1";
		
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberNickNameCheck", method=RequestMethod.POST)
	public String memberNickNameCheckGet(String nickName) {
		String res = "0";
		MemberVO vo = memberService.getMemberNickNameCheck(nickName);
		
		if(vo != null) res = "1";
		
		return res;
	}
	/*
	@RequestMapping(value="/memberList", method=RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false)	int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false)	int pageSize) {
		
		// 1. 페이지(pag)를 결정 = 1
		// pagination에서 만든 totRecCnt를 호출만 하면 됨
		int totRecCnt = memberService.totRecCnt(); // 3. 총 레코드 건수를 구함
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1; // 4. 전체 페이지 건수를 구함
		int startIdxNo = (pag - 1) * pageSize; // 5. 현재 페이지의 시작 index 번호를 구함
		int curScrStartNo = totRecCnt - startIdxNo; // 6. 현재 화면에 보여주는 시작번호를 구함
		
		// 블록 페이징 처리 (3단계) => 블록의 시작번호를 0번부터 처리
		int blockSize = 3; // 1. 블록의 크기를 결정 => '3' 
		int curBlock = (pag - 1) / blockSize;// 2. 현재 페이지가 위치하고 있는 블록 번호를 구함 (ex) 1페이지 = 0블록, 3페이지 = 0블록, 5페이지 = 1블록)
		int lastBlock = (totPage - 1) / blockSize; // 3. 마지막 블록 구함
		
		ArrayList<MemberVO> vos = memberService.getMemberList(startIdxNo, pageSize);
		
		model.addAttribute("vos", vos);// 이렇게 담으면 첫 페이지만 나옴
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		model.addAttribute("pageSize", pageSize);
		
		return "member/memberList";
	}
	*/
	
	/* 전체 리스트와 검색 리스트를 하나의 메소드로 처리( => 동적쿼리) */
	/*
	@RequestMapping(value="/memberList", method=RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false)	int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false)	int pageSize) {
		
		// 1. 페이지(pag)를 결정 = 1
		// pagination에서 만든 totRecCnt를 호출만 하면 됨
		int totRecCnt = memberService.totTermRecCnt(mid); // 3. 총 레코드 건수를 구함
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1; // 4. 전체 페이지 건수를 구함
		int startIdxNo = (pag - 1) * pageSize; // 5. 현재 페이지의 시작 index 번호를 구함
		int curScrStartNo = totRecCnt - startIdxNo; // 6. 현재 화면에 보여주는 시작번호를 구함
		
		// 블록 페이징 처리 (3단계) => 블록의 시작번호를 0번부터 처리
		int blockSize = 3; // 1. 블록의 크기를 결정 => '3' 
		int curBlock = (pag - 1) / blockSize;// 2. 현재 페이지가 위치하고 있는 블록 번호를 구함 (ex) 1페이지 = 0블록, 3페이지 = 0블록, 5페이지 = 1블록)
		int lastBlock = (totPage - 1) / blockSize; // 3. 마지막 블록 구함
		
		ArrayList<MemberVO> vos = memberService.getTermMemberList(startIdxNo, pageSize, mid);
		
		model.addAttribute("vos", vos);// 이렇게 담으면 첫 페이지만 나옴
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		model.addAttribute("pageSize", pageSize);
		
		model.addAttribute("mid", mid);
		model.addAttribute("totRecCnt", totRecCnt);
		
		return "member/memberList";
	}
	*/
	
	/* Pagination 이용하기 */
	@RequestMapping(value="/memberList", method=RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false)	int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false)	int pageSize) {
		
		// pagination에서 만든 totRecCnt를 호출만 하면 됨
		// 매개변수에는 뭐가 올지 모르므로, 모두 적어둬야 함
		// pag, pageSize 를 알아야 값 구할 수 있음 (필수) -> 매개변수로 넘겨야 함
		// 어디에서 사용할 것인지? -> member에서 사용한다.
		// 3번째 매개변수까지 필수이고 뒤에 공백으로 둔 것은 확장성을 위해 임의로 넣어줌 (이게 없다면 나중에 추가하기 힘듦)
		// PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", "");
		
		// 검색기 -> 과제???
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", "");
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIdxNo(), pageSize);
		
		model.addAttribute("vos", vos);// 이렇게 담으면 첫 페이지만 나옴
		model.addAttribute("pageVo", pageVo);
		// model.addAttribute("pageSize", pageSize);
		
		return "member/memberList";
	}
	
}
