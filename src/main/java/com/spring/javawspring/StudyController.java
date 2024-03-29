package com.spring.javawspring;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.common.ARIAUtil;
import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.service.StudyService;
import com.spring.javawspring.vo.ChartVO;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.MailVO;
import com.spring.javawspring.vo.MemberVO;
import com.spring.javawspring.vo.PayMentVO;
import com.spring.javawspring.vo.QrCodeVO;
import com.spring.javawspring.vo.TransactionVO;

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
	@RequestMapping(value="/calendar", method=RequestMethod.GET)
	public String calendarGet() {
		studyService.getCalendar();
		return "study/calendar/calendar";
	}
	
	/* QR Code 작성 폼 */
	@RequestMapping(value="/qrCode", method=RequestMethod.GET)
	public String qrCodeGet(HttpSession session, Model model) {
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		model.addAttribute("vo", vo);
		
		return "study/qrCode/qrCode";
	}
	
	// QR Code 생성하기 (선생님 코드! 원본)
	/*
	@ResponseBody
	@RequestMapping(value = "/qrCode", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String qrCodePost(HttpServletRequest request,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="moveFlag", defaultValue = "", required = false) String moveFlag) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		
		String qrCodeName = studyService.qrCreate(mid, moveFlag, realPath);
		
		return qrCodeName;
	}
	*/
	
	/* QR Code 생성 */
	@ResponseBody
	@RequestMapping(value="/qrCode", method=RequestMethod.POST, produces = "application/text; charset=utf8")
	public String qrCodePost(HttpServletRequest request, QrCodeVO vo,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid, 
			@RequestParam(name="moveFlag", defaultValue = "", required = false) String moveFlag,
			String film, String theater, String time, int ticket) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		String qrCodeName = studyService.qrCreate(mid, moveFlag, realPath);
		
		String bigo = film + " / "+ theater + " / " +  time + " / " + ticket;
		// System.out.println("bigo : " + bigo);
		String idx = qrCodeName.substring(qrCodeName.lastIndexOf("_")+1);
		
		vo.setIdx(idx);
		vo.setBigo(bigo);
		vo.setQrCode(qrCodeName);
		studyService.setMovieReservation(vo);
		
		return qrCodeName;
	}
	
	/* QR 고유번호 예약 조회 처리 */
	@ResponseBody
	@RequestMapping(value="/qrSearch", method=RequestMethod.POST, produces = "application/text; charset=utf8")
	public String qrSearchPost(@RequestParam(name="idxSearch", defaultValue = "", required = false) String idxSearch) {
		QrCodeVO vo = studyService.getMovieReservation(idxSearch);
		/* if(vo == null || !idxSearch.equals(vo.getIdx())) { */
		if(vo == null) return "";
		else {
			String bigo = vo.getBigo(); 
			return bigo;
		}
	}
	
	//QR Code 생성하기2 (선생님)
	@ResponseBody
	@RequestMapping(value = "/qrCode2", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String qrCode2Post(HttpServletRequest request,
			@RequestParam(name="moveFlag", defaultValue = "", required = false) String moveFlag) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		
		String qrCodeName = studyService.qrCreate2(moveFlag, realPath);
		
		return qrCodeName;
	}
	
	// QR Code 내역 검색하기 (선생님)
	@ResponseBody
	@RequestMapping(value = "/qrCodeSearch", method = RequestMethod.POST)
	public QrCodeVO qrCodeSearchPost(HttpServletRequest request,
			@RequestParam(name="idx", defaultValue = "", required = false) String idx) {
		// QrCodeVO vo = studyService.qrCodeSearch(idx);
		// System.out.println("vo : " + vo);
		return studyService.qrCodeSearch(idx);
	}
	
	/* 카카오 기본지도 */
	@RequestMapping(value="/kakaomap/kakaomap", method = RequestMethod.GET)
	public String kakaomapGet() {
		return "study/kakaomap/kakaomap";
	}
	
	/* 카카오 '마커표시/DB저장' */
	@RequestMapping(value="/kakaomap/kakaoEx1", method = RequestMethod.GET)
	public String kakaoEx1Get() {
		return "study/kakaomap/kakaoEx1";
	}
	
	/* 카카오 마커표시 DB저장 처리 */
	@ResponseBody
	@RequestMapping(value="/kakaomap/kakaoEx1", method = RequestMethod.POST)
	public String kakaoEx1Post(KakaoAddressVO vo) {
		KakaoAddressVO searchVo = studyService.getKakaoAddressName(vo.getAddress());
		if(searchVo != null) return "0";
		studyService.setKakaoAddressName(vo);
		return "1";
	}
	
	/* 카카오 'DB저장'된 지역 검색 */
	@RequestMapping(value="/kakaomap/kakaoEx2", method = RequestMethod.GET)
	public String kakaoEx2Get(Model model,
			@RequestParam(name="address", defaultValue = "사창사거리", required = false) String address) {
		KakaoAddressVO vo = studyService.getKakaoAddressName(address);
		List<KakaoAddressVO> vos = studyService.getAddressNameList();
		
		model.addAttribute("vo", vo);
		model.addAttribute("vos", vos);
		model.addAttribute("address", address);
		return "study/kakaomap/kakaoEx2";
	}
	
	/* 카카오 'DB저장'된 지역의 삭제 */
	@ResponseBody
	@RequestMapping(value="/kakaomap/kakaoEx2Delete", method = RequestMethod.POST)
	public String kakaoEx2DeletePost(String address) {
		studyService.setKakaoAddressDelete(address);
		
		return "";
	}
	
	/* 카카오 'DB저장'된 지역 검색 */
	@RequestMapping(value="/kakaomap/kakaoEx3", method = RequestMethod.GET)
	public String kakaoEx3Get(Model model,
			@RequestParam(name="address", defaultValue = "사창사거리", required = false) String address) {
		KakaoAddressVO vo = studyService.getKakaoAddressName(address);
		List<KakaoAddressVO> vos = studyService.getAddressNameList();
		
		model.addAttribute("vo", vo);
		model.addAttribute("vos", vos);
		model.addAttribute("address", address);
		return "study/kakaomap/kakaoEx3";
	}
	
	/* 카카오 마커표시 DB저장 처리 */
	@ResponseBody
	@RequestMapping(value="/kakaomap/kakaoEx3", method = RequestMethod.POST)
	public String kakaoEx3Post(KakaoAddressVO vo) {
		KakaoAddressVO searchVo = studyService.getKakaoAddressName(vo.getAddress());
		if(searchVo != null) return "0";
		studyService.setKakaoAddressName(vo);
		return "1";
	}
	
	/* 카카오 거리계산(Ex4) 뷰 */
	@RequestMapping(value="/kakaomap/kakaoEx4", method = RequestMethod.GET)
	public String kakaoEx4Get(Model model,
		@RequestParam(name="address", defaultValue = "사창사거리", required = false) String address) {
			KakaoAddressVO vo = studyService.getKakaoAddressName(address);
			List<KakaoAddressVO> vos = studyService.getAddressNameList();
			
			model.addAttribute("vo", vo);
			model.addAttribute("vos", vos);
			model.addAttribute("address", address);
			
			return "study/kakaomap/kakaoEx4";
	}
	
	/* 카카오 거리계산(Ex5) 뷰 */
	@RequestMapping(value="/kakaomap/kakaoEx5", method = RequestMethod.GET)
	public String kakaoEx5Get(Model model) {
		List<KakaoAddressVO> vos = studyService.getDistanceList();
		
		model.addAttribute("vos", vos);
		return "study/kakaomap/kakaoEx5";
	}

	/* 트랜잭션 폼 호출 */
	@RequestMapping(value="/transaction/transaction", method=RequestMethod.GET)
	public String transactionGet() {
		return "study/transaction/transaction";
	}
	
	/* 트랜잭션 입력 1번폼 (개별) 처리 */
	// @Transactional : 트랜잭션 어노테이션 -> 컨트롤러에 작성해도 되지만, 가급적 Service에 작성!
	@Transactional
	@RequestMapping(value="/transaction/input1", method=RequestMethod.POST)
	public String transactionInput1Post(TransactionVO vo) {
		studyService.setTransInput1(vo);
		studyService.setTransInput2(vo);
		
		return "study/transaction/transaction";
	}
	
	/* 트랜잭션 입력 2번폼 (일괄) 처리 */
	@RequestMapping(value="/transaction/input2", method=RequestMethod.POST)
	public String transactionInput2Post(TransactionVO vo) {
		//studyService.setTransInput1(vo);
		//studyService.setTransInput2(vo);
		
		// 위의 두줄을 아래 한줄로 작성 처리
		studyService.setTransInput(vo); // user, user2에 등록
		
		return "study/transaction/transaction";
	}
	
	
	// 트랜잭션 리스트
	@RequestMapping(value = "/transaction/transactionList", method=RequestMethod.GET) // 로케이션으로 왔으므로 GET
	public String transactionListGet(Model model) {
		List<TransactionVO> vos = studyService.setTransList();
		model.addAttribute("vos", vos);
		
		return "study/transaction/transactionList";
	}
	
	// 결제 연습하기 폼..
	@RequestMapping(value = "/merchant", method = RequestMethod.GET)
	public String merchantGet() {
		return "study/merchant/merchant";
	}
	
	// 결제창 호출하기
	@RequestMapping(value = "/merchant", method = RequestMethod.POST)
	public String merchantPost(Model model, HttpSession session, PayMentVO vo) {
		// 꼭 session에 담아야 함!
		session.setAttribute("sPayMentVO", vo);
		// 정보들은 model에 담기
		model.addAttribute("vo", vo);
		return "study/merchant/sample";
	}
	
	// 결제창 호출하기
	@RequestMapping(value = "/merchantOk", method = RequestMethod.GET)
	public String merchantOkGet(Model model, HttpSession session) {
		PayMentVO vo = (PayMentVO) session.getAttribute("sPayMentVO");
		model.addAttribute("vo", vo);
		session.removeAttribute("sPayMentVO");
		return "study/merchant/merchantOk";
	}
	
	
	// 캡차(컴퓨터와 사람을 구분하기 위한 완전 자동화된 공개 튜링 테스트) - 이미지 제작및 실행
	@RequestMapping(value = "/captchaImage", method = RequestMethod.GET)
	public String captchaPost(HttpServletRequest request, HttpServletResponse response) {
	  try {
	    // 알파벳 숫자섞인 5자리 문자열을 랜덤하게 생성
	    String randomString = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
	    // System.out.println("randomString : " + randomString);
	
	    // 세션에 저장
	    request.getSession().setAttribute("CAPTCHA", randomString);
	
	    // 시스템에 등록된 폰트들 이름을 확인
	    /*
	    Font[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	    for( Font f : fontList){
	        System.out.println(f.getName());
	    }
	     */
	    Font font = new Font("Jokerman", Font.ITALIC, 30);
	    FontRenderContext frc = new FontRenderContext(null, true, true);
	    Rectangle2D bounds = font.getStringBounds(randomString, frc);
	    int w = (int) bounds.getWidth();
	    int h = (int) bounds.getHeight();
	
	    // 이미지 생성
	    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = image.createGraphics();
	    g.setColor(Color.WHITE);
	    g.fillRect(0, 0, w, h);
	    g.setColor(new Color(0, 156, 240));
	    g.setFont(font);
	    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g.drawString(randomString, (float) bounds.getX(), (float) -bounds.getY());
	    g.dispose();
	
	    // ImageIO.write(image, "png",  response.getOutputStream());  // OutputStream을 이용해 헤더를 통해 직접 클라이언트로 전송
	
	    String realPath = request.getSession().getServletContext().getRealPath("/resources/images/");
	    ImageIO.write(image, "png", new File(realPath + "captcha.png"));
	  } catch (IOException e) {
	    e.printStackTrace();
	  }
	  return "study/captcha/captcha";
	}

	 // 캡차(컴퓨터와 사람을 구분하기 위한 완전 자동화된 공개 튜링 테스트) - 폼 보기
	 @RequestMapping(value = "/captcha", method = RequestMethod.GET)
	 public String captchaGet() {
		 // return "study/captcha/captcha";
		 return "redirect:/study/captchaImage";
	 }
	
	 // 캡차(컴퓨터와 사람을 구분하기 위한 완전 자동화된 공개 튜링 테스트) - 결과 비교하여 통보하기
	 @ResponseBody
	 @RequestMapping(value = "/captcha", method = RequestMethod.POST)
	 public String captchaPost(HttpSession session, String strCaptcha) {
	   if(strCaptcha.equals(session.getAttribute("CAPTCHA").toString())) return "1";
	   else return "0";
	 }
	 
	 // 썸네일생성하기 폼 보기
	 @RequestMapping(value = "/thumbnail", method = RequestMethod.GET)
	 public String thumbnailGet() {
	 	return "study/thumbnail/thumbnail";
	 }
	 
	 // 썸네일 만들기
	 @RequestMapping(value = "/thumbnail", method = RequestMethod.POST)
	 public String thumbnailPost(MultipartFile file) {
	 	int res = studyService.thumbnailCreate(file);
			if(res == 1) return "redirect:/msg/thumbnailCreateOk";
			else  return "redirect:/msg/thumbnailCreateNo";
	 }
	 
	 // 썸네일결과 폼 보기
	 @RequestMapping(value = "/thumbnailResult", method = RequestMethod.GET)
	 public String thumbnailResultGet(HttpServletRequest request, Model model) {
			String realPath = request.getSession().getServletContext().getRealPath("/resources/data/thumbnail/");
			
			String[] files = new File(realPath).list();
			
			model.addAttribute("files", files);
	 		
	 	return "study/thumbnail/thumbnailResult";
	 }

	// 선택된 파일 삭제처리하기
	@ResponseBody
	@RequestMapping(value = "/thumnailDelete", method = RequestMethod.POST)
	public String thumnailDeleteGet(HttpServletRequest request, String delItems) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/thumbnail/");
		
		String[] fileNames = delItems.split("/");
		
		for(String fileName : fileNames) {
			String realPathFile = realPath + fileName;
			new File(realPathFile).delete();
		}
		
		return "";
	}

	// 구글차트만들기
	@RequestMapping(value="/googleChart", method=RequestMethod.GET)
	public String googleChartGet(Model model,
			@RequestParam(name="part", defaultValue="bar", required=false) String part) {
		model.addAttribute("part", part);
		return "study/chart/chart";
	}
	
	// 구글차트만들기2 - 자료 입력하여 차크 만들기
	@RequestMapping(value="/googleChart2", method=RequestMethod.GET)
	public String googleChartGet2(Model model,
			@RequestParam(name="part", defaultValue="bar", required=false) String part) {
		model.addAttribute("part", part);
		return "study/chart2/chart";
	}
	
	@RequestMapping(value="/googleChart2", method=RequestMethod.POST)
	public String googleChart2Post(Model model,
			ChartVO vo) {
		model.addAttribute("vo", vo);
		return "study/chart2/chart";
	}
	
	// 최근 방문자수 차트로 표시하기
	@RequestMapping(value="/googleChart2Recently", method=RequestMethod.GET)
	public String googleChart2RecentlyGet(Model model,
			@RequestParam(name="part", defaultValue="line", required=false) String part) {
		//System.out.println("part : " + part);
		List<ChartVO> vos = null;
		if(part.equals("lineChartVisitCount")) {
			vos = studyService.getRecentlyVisitCount(1);
			// vos로 차트에서 처리가 잘 안되는것 같아서 다시 배열로 담아서 처리해본다.
			String[] visitDates = new String[7];
			int[] visitDays = new int[7];	// line차트는 x축과 y축이 모두 숫자가 와야하기에 날짜중에서 '일'만 담기로 한다.(정수타입으로)
			int[] visitCounts = new int[7];
			for(int i=0; i<7; i++) {
				visitDates[i] = vos.get(i).getVisitDate();
				visitDays[i] = Integer.parseInt(vos.get(i).getVisitDate().toString().substring(8));
				visitCounts[i] = vos.get(i).getVisitCount();
			}
			
			model.addAttribute("title", "최근 7일간 방문횟수");
			model.addAttribute("subTitle", "최근 7일동안 방문한 해당일자 방문자 총수를 표시합니다.");
			model.addAttribute("visitCount", "방문횟수");
			model.addAttribute("legend", "일일 방문 총횟수");
			model.addAttribute("topTitle", "방문날짜");
			model.addAttribute("xTitle", "방문날짜");
			model.addAttribute("part", part);
//			model.addAttribute("vos", vos);
			model.addAttribute("visitDates", visitDates);
			model.addAttribute("visitDays", visitDays);
			model.addAttribute("visitCounts", visitCounts);
		}
		
		return "study/chart2/chart";
	}
	
	// 많이찾은 방문자 7명 차트로 표시하기
	@RequestMapping(value="/googleChart2Recently2", method=RequestMethod.GET)
	public String googleChart2Recently2Get(Model model,
			@RequestParam(name="part", defaultValue="line", required=false) String part) {
		List<ChartVO> vos = null;
		if(part.equals("lineChartVisitCount2")) {
			vos = studyService.getRecentlyVisitCount(2);
//			System.out.println("part : " + part);
//			System.out.println("vos : " + vos);
			// vos로 차트에서 처리가 잘 안되는것 같아서 다시 배열로 담아서 처리해본다.
			String[] visitDates = new String[7];
			int[] visitDays = new int[7];	// line차트는 x축과 y축이 모두 숫자가 와야하기에 날짜중에서 '일'만 담기로 한다.(정수타입으로)
			int[] visitCounts = new int[7];
			for(int i=0; i<7; i++) {
				visitDates[i] = vos.get(i).getVisitDate();
				visitDays[i] = 7 - i;
				visitCounts[i] = vos.get(i).getVisitCount();
			}
			
			model.addAttribute("title", "많이 방문한 회원 7명");
			model.addAttribute("subTitle", "가장 많이 방문한 방문자 7인을 표시합니다.");
			model.addAttribute("visitCount", "방문횟수");
			model.addAttribute("legend", "방문 총횟수");
			model.addAttribute("topTitle", "회원아이디");
			model.addAttribute("xTitle", "회원아이디");
			model.addAttribute("part", part);
//			model.addAttribute("vos", vos);
			model.addAttribute("visitDates", visitDates);
			model.addAttribute("visitDays", visitDays);
			model.addAttribute("visitCounts", visitCounts);
		}
		
		return "study/chart2/chart";
	}
	
}
