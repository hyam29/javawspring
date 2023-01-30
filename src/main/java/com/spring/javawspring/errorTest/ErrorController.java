package com.spring.javawspring.errorTest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errorPage")
public class ErrorController {
	
	/* 에러 연습 폼 */
	@RequestMapping(value="/error")
	public String errorGet() {
		return "errorPage/errorMain";
	}
	
	/* JSP파일에서의 에러발생 폼 호출 */
	@RequestMapping(value="/error1")
	public String error1Get() {
		return "errorPage/error1";
	}
	
	/* 웹에서 400, 404, 405, 500 에러 발생 시 이동할 경로 설정 */
	@RequestMapping(value="/errorOk")
	public String errorOkGet() {
		return "errorPage/errorMsg2";
	}
	
	/* Servlet에서의 servlet 오류 발생 */
  // @RequestMapping(value="/error2") 
  // public String error2Get() { 
  // 	return "errorPage/error2"; 
	// }
	 
	
	/* Servlet에서의 예외오류 발생 */
	@RequestMapping(value="/error3")
	public String error3Get() {
		String str = null;
		// null 을 String 타입으로 변경불가 -> 500번 error (nullPointExcetion)
		System.out.println("str : " + str.toString());
		return "errorPage/errorMain";
	}
}
