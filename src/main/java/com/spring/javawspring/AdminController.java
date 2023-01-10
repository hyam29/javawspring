package com.spring.javawspring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.AdminService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PageProcess pageProcess;
	
	@Autowired
	JavaMailSender mailSender;
	
	/* 관리자 페이지 */
	@RequestMapping(value="/adminMain", method=RequestMethod.GET)
	public String adminMainGet() {
		return "admin/adminMain";
	}
	
	@RequestMapping(value="/adminLeft", method=RequestMethod.GET)
	public String adminLeftGet() {
		return "admin/adminLeft";
	}
	
	@RequestMapping(value="/adminContent", method=RequestMethod.GET)
	public String adminContentGet() {
		return "admin/adminContent";
	}
	
	/* Pagination 이용하기 */
	@RequestMapping(value="/member/adminMemberList", method=RequestMethod.GET)
	public String adminMemberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false)	int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false)	int pageSize) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIdxNo(), pageSize, mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		// model.addAttribute("pageSize", pageSize);
		
		return "admin/member/adminMemberList";
	}
	
	/* 회원 등급 변경 처리(ajax) */
	@ResponseBody
	@RequestMapping(value="/member/adminMemberLevel", method=RequestMethod.POST)
	public String adminMemberLevelPost(int idx, int level) {
		int res = adminService.setMemberLevelCheck(idx, level);
		return res+"";
	}
	
	/* 회원 탈퇴 처리(ajax) */
	@ResponseBody
	@RequestMapping(value="/member/adminMemberDel", method=RequestMethod.POST)
	public String adminMemberDelPost(int idx) {
		int res = adminService.setAdminMemberDel(idx);
		System.out.println("res : "+res);
		return res+"";
	}
	
	
	
}
