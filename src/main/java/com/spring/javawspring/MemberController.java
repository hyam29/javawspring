package com.spring.javawspring;

import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MailVO;
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
	
	@Autowired
	JavaMailSender mailSender;
	
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
	
	/* 카카오 로그인 완료후 수행할 내용들을 기술한다. */
	@RequestMapping(value="/memberKakaoLogin", method=RequestMethod.GET)
	public String memberKakaoLoginGet(HttpSession session, HttpServletRequest request, HttpServletResponse response, MailVO mailVo,
			String nickName,
			String email) {
		
		// 카카오로그인한 회원이 현재 우리 회원인지를 조회한다.
		// 이미 가입된 회원이라면 서비스를 사용하게 하고, 그렇지 않으면 강제로 회원 가입시킨다.
		MemberVO vo = memberService.getMemberNickNameEmailCheck(nickName, email);
		
		// 현재 우리회원이 아니면 자동회원가입처리..(가입필수사항: 아이디,닉네임,이메일) - 아이디는 이메일주소의 '@'앞쪽 이름을 사용하기로 한다.
		if(vo == null) {
			// 아이디 결정하기
			String mid = email.substring(0, email.indexOf("@"));
			
			// 임시 비밀번호 발급하기(UUID 8자리로 발급하기로 한다. -> 발급후 암호화시켜 DB에 저장)
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0,8);
			
			// 새로 발급된 임시비밀번호를 메일로 전송처리한다.
			try {
				String toMail = email;
				String title = "GreenMuseum 회원가입을 환영합니다! 임시비밀번호를 발급하오니 확인 후 변경해주세요!";
				String content = "";
				
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
				content += "<br><hr><h3><font color='grey'>발급된 임시비밀번호는 다음과 같습니다.<font><h3><hr><br>";
				content += "<br><hr><h3><font color='blue'>"+pwd+"<font><h3><hr><br>";
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
				
				
				// 메일 전송하기
				mailSender.send(message);
				
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			// 메일을 보낸 후 임시비밀번호를 인코딩 처리
			session.setAttribute("sImsiPwd", pwd);	// 임시비밀번호를 발급하여 로그인후 변경처리하도록 한다.
			pwd = passwordEncoder.encode(pwd);
			
			// 자동 회원 가입처리한다.
			memberService.setKakaoMemberInputOk(mid, pwd, nickName, email);
			
			// 가입 처리된 회원의 정보를 다시 읽어와서 vo에 담아준다.
			vo = memberService.getMemberIdCheck(mid);
		}
		// 만약에 탈퇴신청한 회원이 카카오로그인처리하였다라면 'userDel'필드를 'NO'로 업데이트한다.
		if(!vo.getUserDel().equals("NO")) {
			memberService.setMemberUserDelCheck(vo.getMid());
		}
		
		// 회원 인증처리된 경우 수행할 내용? strLevel처리, session에 필요한 자료를 저장, 쿠키값처리, 그날 방문자수 1 증가(방문포인트도 증가), ..
		String strLevel = "";
		if(vo.getLevel() == 0) strLevel = "관리자";
		else if(vo.getLevel() == 1) strLevel = "운영자";
		else if(vo.getLevel() == 2) strLevel = "우수회원";
		else if(vo.getLevel() == 3) strLevel = "정회원";
		else if(vo.getLevel() == 4) strLevel = "준회원";
		
		session.setAttribute("sLevel", vo.getLevel());
		session.setAttribute("sStrLevel", strLevel);
		session.setAttribute("sMid", vo.getMid());
		session.setAttribute("sNickName", vo.getNickName());
		
		// 로그인한 사용자의 오늘 방문횟수(포인트) 누적...
		memberService.setMemberVisitProcess(vo);
		
		return "redirect:/msg/memberLoginOk?mid="+vo.getMid();
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
	public String memberJoinPost(MultipartFile fName, MemberVO vo) {
		// 동일 ID, 닉네임 백엔드 중복 체크 (중요한 부분만 백엔드에서 중복확인! 참고: 유효성검사는 VO에서 함)
		if(memberService.getMemberIdCheck(vo.getMid()) != null) {
			return "redirect:/msg/memberIdCheckNo";
		}
		if(memberService.getMemberNickNameCheck(vo.getNickName()) != null) {
			return "redirect:/msg/memberNickNameCheckNo";
		}
		
		// 비밀번호 암호화(BCryptPasswordEncoder)
		vo.setPwd(passwordEncoder.encode(vo.getPwd()));
		
		// 체크가 완료되면 사진파일 업로드 후, vo에 담긴 자료를 DB에 저장! (회원가입) -> 서비스 객체에서 수행 처리
		int res = memberService.setMemberJoinOk(fName, vo);
		
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
		
		// 검색기 처리 (4번째 "" -> part)
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIdxNo(), pageSize, mid);
		
		model.addAttribute("vos", vos);// 이렇게 담으면 첫 페이지만 나옴
		model.addAttribute("pageVo", pageVo);
		// model.addAttribute("pageSize", pageSize);
		model.addAttribute("mid", mid);
		return "member/memberList";
	}

	@RequestMapping(value="/memberDelete", method=RequestMethod.GET)
	public String memberDeleteGet() {
		return "member/memberDelete";
	}
	
	@RequestMapping(value="/memberDelete", method=RequestMethod.POST)
	public String memberDeletePost(
			@RequestParam(name="mid", defaultValue = "", required = false) String mid, 
			@RequestParam(name="pwd", defaultValue = "", required = false) String pwd) {
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(!passwordEncoder.matches(pwd, vo.getPwd())) {
			return "redirect:/msg/memberPwdNo";
		}
		else if(passwordEncoder.matches(pwd, vo.getPwd())) {
			memberService.setMemberDelete(mid);
		}
		return "redirect:/msg/memberDeleteOk";
	}
	
	@RequestMapping(value="/memberPwdSearch", method=RequestMethod.GET)
	public String memberPwdSearchGet() {
		return "member/memberPwdSearch";
	}
	
	/* 비밀번호 찾기를 위한 임시비밀번호 발급 처리 (임시비밀번호를 생성시켜 메일로 보내기) */
	@RequestMapping(value="/memberPwdSearch", method=RequestMethod.POST)
	public String memberPwdSearchPost(String mid, String toMail) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(vo.getEmail().equals(toMail)) {
			// 회원정보가 맞다면, 임시비밀번호를 발급
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0, 8);
			
			// 임시비밀번호를 암호화(인코딩)처리시켜 DB에 저장
			memberService.setMemberPwdUpdate(mid, passwordEncoder.encode(pwd));
			String content = pwd;
			
			// 임시비밀번호를 메일로 전송 처리 (mailSend 메소드 생성)
			String res = mailSend(toMail, content);
			
			if(res.equals("1")) return "redirect:/msg/mamberImsiPwdOk";
			else return "redirect:/msg/mamberImsiPwdNo";
		}
		else {
			return "redirect:/msg/mamberImsiPwdNo";
		}
	}
	
	// public 접근제한자로 mailSend 생성! (나중에 또 사용될 수도 있으니까)
	public String mailSend(String toMail, String content) {
		try {
			String title = "우리 사이트에서 발급된 임시비밀번호입니다.";
			
			// 메일을 전송하기 위한 객체 : MimeMessage(), MimeMessageHelper()(보관함)
			// JavaMailSender 위에 @Autowired 어노테이션 해둬야 함!
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			
			// 메일보관함에 회원이 보내온 메세지들을 모두 저장시킴
			messageHelper.setTo(toMail); // 위에 변수 선언안하고 여기에 vo.getToMail()로 작성해도 됨.
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			// 전송 시, 내용(content)이 없더라도 그냥 보내짐. 따라서, 정보를 좀 더 추가해서 보내는 것이 좋음!
			
			// 메세지 보관함의 내용(content) 편집 후 필요한 정보를 좀 더 추가해서 전송 처리
			// project때는, 방문하기 주소를 memberLogin으로 보내면 됨!
			content = "<br><hr><h3><font color='blue'>임시 비밀번호는<font color='red'>"+content+"</font>입니다.<font><h3><hr><br>";
			content += "<br><hr><h3><font color='grey'>우리 사이트에 로그인 하여 비밀번호를 변경해주시기 바랍니다.<font><h3><hr><br>";
			content += "<p>방문하기 ▶ <a href='http://49.142.157.251:9090/green2209J_04/'>그린현대미술관</a></p>";
			// 경로 작은따옴표 안먹을 때 있어서, ""로 작성
			content += "<p><img src=\"cid:main.png\" width='500px' ></p>";
			content += "<br><hr><h3><font color='grey'>from. Green Museum<font><h3><hr><br>";
			content += "<hr>";
			// 편집된 content로 보내겠다.
			messageHelper.setText(content, true);

			// 기재된 그림파일의 경로를 따로 표시처리 한 후 보관함에 다시 저장 (순서 중요)
			FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\main.png");
			// 그림파일 저장("그림파일명", 객체변수(file))
			messageHelper.addInline("main.png", file);
			
			// 메일 전송하기
			mailSender.send(message);
			return "1";
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "0";
	}
	
	/* 비밃전호 수정 처리 폼 */
	@RequestMapping(value="/memberPwdUpdate", method=RequestMethod.GET)
	public String memberPwdUpdateGet() {
		return "member/memberPwdUpdate";
	}
	
	/* 비밀번호 수정 처리 */
	@RequestMapping(value="/memberPwdUpdate", method=RequestMethod.POST)
	public String memberPwdUpdatePost(String mid, String oldPwd, String newPwd, HttpSession session) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		// 카카오로그인으로 비밀번호 변경한 사용자는 임시비밀번호 세션을 삭제한다.
		if(session.getAttribute("sImsiPwd") != null) session.removeAttribute("sImsiPwd");
		
		if(!passwordEncoder.matches(oldPwd, vo.getPwd())) {
			return "redirect:/msg/memberPwdNo";
		}
		else if(passwordEncoder.matches(oldPwd, vo.getPwd())) {
			memberService.setMemberPwdUpdate(mid, passwordEncoder.encode(newPwd));
		}
		return "redirect:/msg/memberPwdUpdateOk";
	}	
	
	/* 아이디 찾기 (과제->완료) */
	@RequestMapping(value="/memberIdSearch", method=RequestMethod.GET)
	public String memberIdSearchGet() {
		return "member/memberIdSearch";
	}	
	
	@ResponseBody
	@RequestMapping(value="/memberIdSearch", method=RequestMethod.POST)
	public String memberIdSearchPost(Model model,
			@RequestParam(name="email", defaultValue = "", required = false) String email) {
		MemberVO vo = memberService.getMemberIdSearch(email);
		String res = "0";
   if(vo == null) {
     res = "0";
   }
   else {
   	res = vo.getMid();
   }
   // response.getWriter().write(mid);
		
   /*
   int midLength = mid.length();
   char[] strMid = mid.toCharArray();
   mid = "";
   for(int i=0; i<midLength; i++) {
    // if(2 < i && i <= 5) strMid[i] = '*';
    // if(8 < i && i <= 11) strMid[i] = '*';
     if(i > 4) strMid[i] = '*';
     mid += strMid[i];
   }
   */
		
		return res;
	}
	
	/* 멤버 상세보기 */
	@RequestMapping(value="/memberInfor", method=RequestMethod.GET)
	public String memberInforGet(Model model, String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false)	int pag) {
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		String strLevel = "";
		if(vo.getLevel() == 0) strLevel = "관리자";
		else if(vo.getLevel() == 1) strLevel = "운영자";
		else if(vo.getLevel() == 2) strLevel = "우수회원";
		else if(vo.getLevel() == 3) strLevel = "정회원";
		else if(vo.getLevel() == 4) strLevel = "준회원";
		
		
		model.addAttribute("vo", vo);
		model.addAttribute("sStrLevel", strLevel);
		model.addAttribute("pag", pag);
		
		return "member/memberInfor";
	}
	
	/* 회원정보 수정 전 비밀번호 확인 폼 */
	@RequestMapping(value="/memberPwdCheck", method=RequestMethod.GET)
	public String memberPwdCheckGet() {
		return "member/memberPwdCheck";
	}
	
	/* 회원정보 수정 전 비밀번호 확인 처리 */
	@RequestMapping(value="/memberPwdCheck", method=RequestMethod.POST)
	public String memberPwdCheckPost(@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pwd", defaultValue = "", required = false) String pwd) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(!passwordEncoder.matches(pwd, vo.getPwd())) {
			return "redirect:/msg/memberUpdatePwdNo";
		}
		else if(passwordEncoder.matches(pwd, vo.getPwd())) {
			memberService.setMemberDelete(mid);
		}
		return "redirect:/msg/memberUpdatePwdOk";
		
	}
	
	/* 회원정보 수정하기 폼 */
	@RequestMapping(value="/memberUpdate", method=RequestMethod.GET)
	public String memberUpdateGet(Model model, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		// JSP Form에 출력을 위한 분리작업 처리
		// email 분리 (@)
		String[] email = vo.getEmail().split("@");
		model.addAttribute("email1", email[0]);
		model.addAttribute("email2", email[1]);
		
		// 전화번호 분리 (-)
		String[] tel = vo.getTel().split("-");
		if(tel[1].equals(" ")) tel[1] = "";
		if(tel[2].equals(" ")) tel[2] = "";
		model.addAttribute("tel1", tel[0]);
		model.addAttribute("tel2", tel[1]);
		model.addAttribute("tel3", tel[2]);
		
		// 주소 분리 (/)
		String[] address = vo.getAddress().split("/");
		if(address[0].equals(" ")) address[0] = "";
		if(address[1].equals(" ")) address[1] = "";
		if(address[2].equals(" ")) address[2] = "";
		if(address[3].equals(" ")) address[3] = "";
		model.addAttribute("postcode", address[0]);
		model.addAttribute("roadAddress", address[1]);
		model.addAttribute("detailAddress", address[2]);
		model.addAttribute("extraAddress", address[3]);
		
		// 취미 분리 (/) => JSP에서 분리할 것임
		model.addAttribute("hobby", vo.getHobby());
		
		// 생일(년-월-일) : 앞에서부터 10자리 만 넘김 (시간 자르기)
		model.addAttribute("birthday", vo.getBirthday().substring(0, 10));
		
		model.addAttribute("vo", vo);
		return "member/memberUpdate";
	}
	
	@RequestMapping(value="/memberUpdate", method=RequestMethod.POST)
	public String memberUpdatePost(MultipartFile fName, MemberVO vo, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		int res = memberService.setMemberUpdate(fName, vo, mid);
		
		if(res == 1) return "redirect:/msg/memberUpdateOk";
		else return "redirect:/msg/memberUpdateNo";
		
	}
	
	
}
