package com.spring.javawspring;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.common.ARIAUtil;
import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.service.StudyService;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.MailVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/study")
public class StudyController {

	@Autowired
	StudyService studyService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	// 우두머리인 인터페이스를 autowired 어노테이션! (class로 생성해도 상관은 없음...)
	@Autowired
	JavaMailSender mailSender;
	
	@RequestMapping(value="/ajax/ajaxMenu", method=RequestMethod.GET)
	public String ajaxMenuGet() {
		return "study/ajax/ajaxMenu";
	}
	
	/* 일반 String 값의 전달 1 (숫자/영문자/특수문자) */
	// @ResponseBody : String 타입의 값을 받게해주는 어노테이션 (ajax 사용시 필수)
	// 위치는 롬복 사용시처럼 상관없음
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest1_1", method=RequestMethod.POST)
	public String ajaxTest1_1Post(int idx) {
		idx = (int)(Math.random()*idx) + 1;
		String res = idx + " : Have a Good Time~~~!";
		return res;
	}
	
	
	/* 일반 String 값의 전달 2 (숫자/영문/한글/특수문자) */
	// produces="" : 응용프로그램 안에서 문자처리 방식을 utf8 방식으로 처리하라. (객체에 든 것(배열)은 한글문제 XXX, 낱개의 문자열(String)이 문제)
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest1_2", method=RequestMethod.POST, produces="application/text; charset=utf8")
	public String ajaxTest1_2Post(int idx) {
		idx = (int)(Math.random()*idx) + 1;
		String res = idx + " : Hello~ 클레오파트라 세상에서 제일가는 포테이토칩!!!";
		return res;
	}
	
	/* 일반(String) 배열값의 전달 폼 */
	@RequestMapping(value="/ajax/ajaxTest2_1", method=RequestMethod.GET)
	public String ajaxTest2_1Get() {
		return "study/ajax/ajaxTest2_1";
	}
	
	/* 일반(String) 배열값의 전달 */
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest2_1", method=RequestMethod.POST)
	public String[] ajaxTest2_1Post(String dodo) {
	//	String[] strArr = new String[100];
	//	strArr = studyService.getCityStringArr(dodo);
	//	return strArr;
		
		// 위의 3줄을 합쳐서 return에 작성! (실무작성법)
		return studyService.getCityStringArr(dodo);
	}
	
	/* 객체(ArrayList) 배열값의 전달 폼 */
	@RequestMapping(value="/ajax/ajaxTest2_2", method=RequestMethod.GET)
	public String ajaxTest2_2Get() {
		return "study/ajax/ajaxTest2_2";
	}
	
	/* 객체(ArrayList) 배열값의 전달 */
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest2_2", method=RequestMethod.POST)
	public ArrayList<String> ajaxTest2_2Post(String dodo) {
		return studyService.getCityArrayListArr(dodo);
	}
	
	/* Map(HashMap<k,v>) 값의 전달 폼 */
	@RequestMapping(value="/ajax/ajaxTest2_3", method=RequestMethod.GET)
	public String ajaxTest2_3Get() {
		return "study/ajax/ajaxTest2_3";
	}
	
	/* Map(HashMap<k,v>) 값의 전달 */
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest2_3", method=RequestMethod.POST)
	public HashMap<Object, Object> ajaxTest2_3Post(String dodo) {
		ArrayList<String> vos = new ArrayList<String>();
		vos = studyService.getCityArrayListArr(dodo);
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		// key 자리지만, 값이 들어있으므로 vos를 city로 작성.
		map.put("city", vos);
		
		return map;
	}
	
	/* DB를 활용한 값의 전달 폼 */
	@RequestMapping(value="/ajax/ajaxTest3", method=RequestMethod.GET)
	public String ajaxTest3Get() {
		return "study/ajax/ajaxTest3";
	}
	
	/* DB를 활용한 값의 전달 1(vo) */
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest3_1", method=RequestMethod.POST)
	public GuestVO ajaxTest3_1Post(String mid) {
		// GuestVO vo = studyService.getGuestMid(mid);
		// return vo;
		// 아래와 같이 한줄로 작성 가능!
		return studyService.getGuestMid(mid);
	}
	
	/* DB를 활용한 값의 전달 2(vos) */
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest3_2", method=RequestMethod.POST)
	public ArrayList<GuestVO> ajaxTest3_2Post(String mid) {
		// ArrayList<GuestVO> vos = studyService.getGuestName(mid);
		// return vos;
		return studyService.getGuestNames(mid);
	}
	
	/* DB를 활용한 값의 전달 과제 (검색기) */
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest3_3", method=RequestMethod.POST)
	public ArrayList<GuestVO> ajaxTest3_3Post(String part, String mid) {
		return studyService.getGuestPart(part, mid);
	}
	
	/* 암호화 연습(SHA256) */
	@RequestMapping(value="/password/sha256", method=RequestMethod.GET)
	public String sha256Get() {
		return "study/password/sha256";
	}
	
	/* 암호화 연습(SHA256) 결과 처리 (한글처리 필수) */
	@ResponseBody
	@RequestMapping(value="/password/sha256", method=RequestMethod.POST, produces="application/text; charset=utf8")
	public String sha256Post(String pwd) {
		String encPwd = SecurityUtil.encryptSHA256(pwd);
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd;
		return pwd;
	}
	
	/* 암호화 연습(ARIA) 폼 */
	@RequestMapping(value="/password/aria", method=RequestMethod.GET)
	public String ariaGet() {
		return "study/password/aria";
	}
	
	/* 암호화 연습(ARIA) 결과 처리 (한글처리 필수) */
	@ResponseBody
	@RequestMapping(value="/password/aria", method=RequestMethod.POST, produces="application/text; charset=utf8")
	public String ariaPost(String pwd) {
		String encPwd = "";
		String decPwd = "";
		// 전자정보framework만 예외처리 필수
		try {
			encPwd = ARIAUtil.ariaEncrypt(pwd); // 암호화
			decPwd = ARIAUtil.ariaDecrypt(encPwd); // 복호화
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd + " / 복호화된 비밀번호 : " + decPwd;
		return pwd;
	}
	
	/* 암호화 연습(bCryptPassword) 폼 */
	@RequestMapping(value="/password/bCryptPassword", method=RequestMethod.GET)
	public String bCryptPasswordGet() {
		return "study/password/security";
	}
	
	/* 암호화 연습(bCryptPassword) 결과 처리 (한글처리 필수) */
	@ResponseBody
	@RequestMapping(value="/password/bCryptPassword", method=RequestMethod.POST, produces="application/text; charset=utf8")
	public String bCryptPasswordPost(String pwd) {
		String encPwd = "";
			encPwd = passwordEncoder.encode(pwd); // 암호화
			
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd;
		return pwd;
	}
	
	/* 주소록 호출하기 */
	
	@RequestMapping(value="/mail/mailForm", method=RequestMethod.GET)
	public String mailFormGet(Model model, String email) {
		
		List<MemberVO> vos = memberService.getMemberList(0, 1000, "");
		
		model.addAttribute("vos", vos);
		model.addAttribute("cnt", vos.size());
		model.addAttribute("email", email);
		
		return "study/mail/mailForm";
	}
	
	/* 메일 전송 처리 (무조건 예외처리 필수) */
	@RequestMapping(value="/mail/mailForm", method=RequestMethod.POST)
	public String mailFormPost(MailVO vo, HttpServletRequest request) {
		try {
			String toMail = vo.getToMail();
			String title = vo.getTitle();
			String content = vo.getContent();
			
			// 메일을 전송하기 위한 객체 : MimeMessage(), MimeMessageHelper()(보관함)
			// mailSender 위에 @Autowired 어노테이션 해둬야 함!
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			
			// 메일보관함에 회원이 보내온 메세지들을 모두 저장시킴
			messageHelper.setTo(toMail); // 위에 변수 선언안하고 여기에 vo.getToMail()로 작성해도 됨.
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			// 전송 시, 내용(content)이 없더라도 그냥 보내짐. 따라서, 정보를 좀 더 추가해서 보내는 것이 좋음!
			
			// 메세지 보관함의 내용(content) 편집 후 필요한 정보를 좀 더 추가해서 전송 처리
			content = content.replace("\n", "<br/>");
			// HTML4 에서는 <br/> 슬래쉬(/) XXX -> 따라서 / 생략해서 작성
			content += "<br><hr><h3><font color='grey'>from. Green Museum<font><h3><hr><br>";
			// 경로 작은따옴표 안먹을 때 있어서, ""로 작성
			content += "<p><img src=\"cid:main.png\" width='500px' ></p>";
			content += "<p>방문하기 ▶ <a href='http://49.142.157.251:9090/green2209J_04/'>그린현대미술관</a></p>";
			content += "<hr>";
			// 편집된 content로 보내겠다.
			messageHelper.setText(content, true);

			// 기재된 그림파일의 경로를 따로 표시처리 한 후 보관함에 다시 저장 (순서 중요)
			FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\main.png");
			// 그림파일 저장("그림파일명", 객체변수(file))
			messageHelper.addInline("main.png", file);
			
			// 첨부파일 보내기(서버 파일시스템에 있는 파일) -> 서버에 올려두고 첨부해야 함!
			// 변수명이 동일해도 상관 없음! (달라도 됨)
			file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\chicago.jpg");
			// 첨부파일 저장("첨부파일명", 객체변수(file))
			messageHelper.addAttachment("chicago.jpg", file);
			
			file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\images.zip");
			messageHelper.addAttachment("images.zip", file);
			
			// 경로 작성이 귀찮으므로, realPath(request객체) 이용 
			// (과제) 앞으로 컨트롤러가 아닌 service에서 이 부분들을 처리해야 하는데, request 객체 이용 X -> jsp pds...? 방법 이용...
			// 방법 1. file = new FileSystemResource(request.getRealPath("/resources/images/paris.jpg"));
			// 방법 2. getRealPath 노란줄(옛날작성법) 없애려면 아래와 같이 작성
			file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/paris.jpg"));
			messageHelper.addAttachment("paris.jpg", file);
			
			
			
			// 메일 전송하기
			mailSender.send(message);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "redirect:/msg/mailSendOk";
	}
	
	/* UUID 입력 폼 */
	@RequestMapping(value="/uuid/uuidForm", method=RequestMethod.GET)
	public String uuidFormGet() {
		return "study/uuid/uuidForm";
	}
	
	/* UUID 처리 */
	@ResponseBody
	@RequestMapping(value="/uuid/uuidProcess", method=RequestMethod.POST)
	public String uuidFormPost() {
		// UUID는 Java.util 내장 객체로 바로 생성 가능
		UUID uid = UUID.randomUUID();
		
		// uid 는 객체라서, toString으로 String 타입 캐스팅
		return uid.toString();
	}
	
	/* 파일 업로드 폼 */
	@RequestMapping(value="/fileUpload/fileUploadForm", method=RequestMethod.GET)
	public String fileUploadFormGet() {
		return "study/fileUpload/fileUploadForm";
	}
	
	/* 파일 업로드 처리 (request객체 사용해야 함) */
	@RequestMapping(value="/fileUpload/fileUploadForm", method=RequestMethod.POST)
	public String fileUploadFormPost(MultipartFile fName) {
		int res = studyService.fileUpload(fName);
		if(res == 1) return "redirect:/msg/fileUploadOk";
		else return "redirect:/msg/fileUploadNo";
	}
	
	/* 달력내역 가져오기 */
	@RequestMapping(value="calendar", method=RequestMethod.GET)
	public String calendarGet() {
		studyService.getCalendar();
		return "study/calendar/calendar";
	}
	
	
}
