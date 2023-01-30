package com.spring.javawspring.errorTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

// @ControllerAdvice
public class ErrorAdvice {

	/* 실무에서의 logger 사용(예외오류) */
	// 모두가 오류메세지를 보면 안되므로 private 접근제한자로 생성
	// slf4j 제공해주는 Logger 사용
	// ErrorAdvice.class : 현재 패키지(erorrTest)에 있는 모든 class 대한 에러를 모두 logger에 담아달라. 
	private Logger logger = LoggerFactory.getLogger(ErrorAdvice.class);
	
	// ControllerAdvice 에서 제공해주는 예외처리 많음! 그중에서 ExceptionHandler 사용
//	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model) {
		
		// logger는 쉼표(,) 뒤의 부분을 앞의 중괄호({}) 안에 넣어줌
		logger.error("예외오류발생 : {}", ex.getMessage());
		System.out.println();
		
		System.out.println("exception : " + ex);
		System.out.println("exception : " + ex.getMessage());
		// model.addAttribute("msg", "예외오류가 발생되었습니다.<br/> 표기법1 : " + ex);
		model.addAttribute("msg", "예외오류가 발생되었습니다.<br/> 표기법2 : " + ex.getMessage());
		return "errorPage/errorMsg3";
	}
	
	/* 404에러가 발생시에 처리하는 메소드(web.xml에서 처리) */
//	@ExceptionHandler(NoHandlerFoundException.class)
//	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public String handle404(NoHandlerFoundException ex, Model model) {
		logger.error("404 요청 오류 발생1 : {}", ex.getMessage());
		logger.error("404 요청 오류 발생2 : {}", ex.getRequestURL());
		
		model.addAttribute("msg", "<p><font color='blue'><b>접속한 페이지 정보가 없습니다. 확인하시고 재접속해주세요.</b></font></p><p><input type='button' value='이전페이지' onclick='history.back();' class='btn btn-outline-dark' /><br/>"+ex.getRequestURL()+"</p>");
		
		return "errorPage/errorMsg3";
	}
	
}
