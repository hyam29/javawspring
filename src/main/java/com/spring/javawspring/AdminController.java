package com.spring.javawspring;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
	public String adminContentGet(Model model) {
		/* 한달 신규 가입자 수 */
		int monthJoin = memberService.getMonthNewUser();
		model.addAttribute("monthJoin", monthJoin);
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
	
	/* ckeditor폴더의 파일 리스트 보여주기 */
	@RequestMapping(value="/file/fileList", method=RequestMethod.GET)
	public String fileListGet(HttpServletRequest request, Model model) {
		String realPath = request.getRealPath("/resources/data/ckeditor/");
		// .list() : 해당 파일에 들어가있는 list를 모두 가져옴 (따라서, 배열에 담아줌)
		String[] files = new File(realPath).list();
		
		model.addAttribute("files", files);
		
		return "admin/file/fileList";
	}
	
	/* 파일관리 선택된 파일 삭제처리하기 */
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value = "/fileSelectDelete", method = RequestMethod.POST)
	public String fileSelectDeleteGet(HttpServletRequest request, String delItems) {
		// System.out.println("delItems : " + delItems);
		String realPath = request.getRealPath("/resources/data/ckeditor/");
		delItems = delItems.substring(0, delItems.length()-1);
		
		String[] fileNames = delItems.split("/");
		
		for(String fileName : fileNames) {
			String realPathFile = realPath + fileName;
			new File(realPathFile).delete();
		}
		
		return "1";
	}
	

	
}
