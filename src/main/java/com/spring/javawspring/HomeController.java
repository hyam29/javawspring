package com.spring.javawspring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = {"/","/h"}, method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	/* CKEditor 파일업로드 백엔드 처리(객체 3개 세트) */
	@RequestMapping("/imageUpload")
	public void imageUploadGet(MultipartFile upload,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		// 필터를 거치지 않고 독립적으로 사용하는 것이므로, 한글처리 인코딩 (아래 두줄은 생략해도 됨)
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String oFileName = upload.getOriginalFilename();
		
		// 동일 파일명 변경 처리 (방법1. UUID, 방법2. 날짜(분류가 편함) -> 날짜! 시분초까지 가져와야 함!)
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		oFileName = sdf.format(date) + "_" + oFileName;
		
		// byte배열로 담아서, 예외처리 throw
		byte[] bytes = upload.getBytes();
		
		// CKEditor에서 올린(전송) 파일을 서버 파일시스템에 실제 저장할 경로 설정 => realPath ($ {ctp} 경로 XXX)
		// String realPath = request.getRealPath(oFileName);
		
		// 작업폴더로 경로 수정 (실제 저장은 board)
		// String realPath = request.getSession().getServletContext().getRealPath("/resources/data/board/");
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/");
		OutputStream os = new FileOutputStream(new File(realPath + oFileName));
		os.write(bytes);
		// 위 까지가 파일 업로드 완료 (multipartfile, request 객체 이용)
		
		/* 서버 파일시스템에 저장된 파일을 브라우저 편집 화면에 보여주기 위한 작업 -> response 객체 이용 */
		PrintWriter out = response.getWriter(); 
		// 아래의 경로는 / = contextPath(ctp)부터 시작되는 경로 작성 (서버에 저장된 url 경로)
		
		// String fileUrl = request.getContextPath() + "/board/" + oFileName;
		String fileUrl = request.getContextPath() + "/data/ckeditor/" + oFileName;
		// Jason 형식 작성 (key 값 작성 시 다음과 같은 양식으로 작성 {"key1":"value1", "key2":"value2"})
		// uploaded(예약어) : 업로드 하는 거니? (1 = true)
		// "" 안에 작성하면 문자로 보므로, \"\" 지우고 1 작성!
		// url(예약어) : 파일 어디서 가져올거야?
		out.println("{\"{originalFilename\":\""+oFileName+"\",\"uploaded\":1,\"url\":\""+fileUrl+"\"}");
		
		out.flush(); // 빠진 거 있음 남은 것도 다 보내줘
		os.close(); // 객체 닫음
	}
	
	@RequestMapping(value = "/webSocket", method = RequestMethod.GET)
	public String webSocketGet(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
		return "webSocket/webSocket";
	}
	
	@RequestMapping(value = "/webSocket/chat", method = RequestMethod.GET)
	public String chatGet(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
		return "webSocket/chat";
	}
	
	@RequestMapping(value = "/webSocketDb", method = RequestMethod.GET)
	public String webSocketDbGet(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
		return "webSocket/webSocketDb";
	}
	
}
