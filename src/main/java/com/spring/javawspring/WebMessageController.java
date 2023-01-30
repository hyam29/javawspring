package com.spring.javawspring;

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
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.service.WebMessageService;
import com.spring.javawspring.vo.MemberVO;
import com.spring.javawspring.vo.WebMessageVO;

@Controller
@RequestMapping("/webMessage")
public class WebMessageController {
	@Autowired
	WebMessageService webMessageService;
	
	@Autowired
	PageProcess pageProcess;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping(value="/webMessage", method = RequestMethod.GET)
	public String webMessageGet(Model model, HttpSession session,
			@RequestParam(name="mSw", defaultValue = "1", required = false) int mSw,
			@RequestParam(name="mFlag", defaultValue = "1", required = false) int mFlag,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(name="receiveSw", defaultValue = "", required = false) String receiveSw,
			@RequestParam(name="idx", defaultValue = "0", required = false) int idx) {
		
		String mid = (String) session.getAttribute("sMid");
		
		if(mSw == 0) { 
			// 메세지작성
			
			// 주소록 처리
			List<MemberVO> memberVos = memberService.getMemberList(0, 1000, "");
			model.addAttribute("memberVos", memberVos);
			model.addAttribute("cnt", memberVos.size());
		}
		else if(mSw == 6) { 
			// 메세지내용상세보기 (idx만 사용)
			WebMessageVO vo = webMessageService.getWmMessageOne(idx, mid, mFlag, receiveSw);
			model.addAttribute("vo", vo);
			model.addAttribute("mFlag", 1);
		}
		else if(mSw == 7) { 
			// 휴지통 비우기 선택 -> 휴지통에 있는 메세지의 receiveSw = 'x' 처리 
			webMessageService.setWmDeleteAll(mid);
			// sendSw와 receiveSw가 모두 'x'인것을 찾아 모두 삭제 처리! (관리자에서도 만들어 두기)
			return "redirect:/msg/wmDeleteAll";
		}
		else { 
			// 받은메세지, 새메세지, 보낸메세지, 수신확인, 휴지통
			
			// 페이징 처리
			PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "webMessage", mid, mSw+"");
			
			// 메세지 리스트 가져오기
			List<WebMessageVO> vos = webMessageService.getWmMessageList(mid, mSw, pageVo.getStartIdxNo(), pageSize);
			
			model.addAttribute("vos", vos);
			model.addAttribute("pageVo", pageVo);
			
		}
		
		model.addAttribute("mSw", mSw);
		
		return "webMessage/wmMessage";
	}
	
	/* 메세지 전송 처리 */
	@RequestMapping(value="/wmInput", method=RequestMethod.POST)
	public String wmInputPost(WebMessageVO vo) {
		MemberVO vo2 = memberService.getMemberIdCheck(vo.getReceiveId());
		if(vo2 == null) return "redirect:/msg/wmMemberIdNo";
		
		
		webMessageService.setWmInputOk(vo);
		
		return "redirect:/msg/wmInputOk";
	}
	
	/* 받은메세지함에서 휴지통으로 삭제 처리 (r->g) */
	@RequestMapping(value="/webDeleteCheck", method=RequestMethod.GET)
	public String webDeleteCheckGet(Model model, int idx, int mSw, int mFlag) {
		webMessageService.setWmDeleteCheck(idx, mFlag);
		
		model.addAttribute("mSw", mSw);
		return "redirect:/webMessage/webMessage";
	}
	
	/* 보낸메세지함 삭제 처리 (s->x) */
	@ResponseBody
	@RequestMapping(value="/wmDelete", method=RequestMethod.POST)
	public String webDeletePost(int idx, int mFlag) {
		webMessageService.setWmDeleteCheck(idx, mFlag);
		
		return "";
	}
	
	
}
