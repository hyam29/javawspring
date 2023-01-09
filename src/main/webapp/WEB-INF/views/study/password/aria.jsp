<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>aria.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		let str = "";
		let cnt = 0;
		
		function ariaCheck() {
		let pwd = $("#pwd").val();
		
		$.ajax({
			type : "post",
			url : "${ctp}/study/password/aria",
			data : {pwd : pwd},
			success : function(res) {
				cnt++;
				str += cnt + " : " + res + "<br/>";
				
				$("#demo").html(str);
			},
			error : function() {
				alert("aria 암호화 전송오류!");
			}
		});
		}
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h2>ARIA 암호화</h2>
	<p>
		ARIA 암호화 방식은 경량환경 및 하드웨어 구현을 위해 최적화된 Involutional SPN 구조를 갖는 범용 블록 암호화 알고리즘<br/>
		ARIA가 사용하는 연산은 대부분 XOR(Exclusive-OR=EOR, 배타적논리합)과 같은 단순한 바이트단위 연산으로, 블록크기는 128bit<br/>
		(XOR 연산이란  배타적 논리합(exclusive OR)이라고도 불리며, 두 개의 피연산자 중 하나만이 1일 때 1을 반환합니다.<br/>
		이러한 성질을 이용하면 비트 NOT 연산자는 모든 비트를 반전시키지만, 비트 XOR 연산자는 지정한 비트만을 반전시킬 수 있습니다.)<br/>
		ARIA(Academy(학계) Research Institute(연구소) Agency(정부기관)의 첫글자를 가져와 만듦)<br/>
			-> ARIA 개발에 참여한 '학/연/관'의 공동노력을 포함<br/>
		AIRA암호화는 복호화 가능.
	</p>
	<hr/>
	<p>
		<input type="text" name="pwd" id="pwd" autofocus />
		<input type="button" value="ARIA암호화" onclick="ariaCheck()" class="btn btn-sm btn-outline-primary" />
		<input type="button" value="다시하기" onclick="location.reload()" class="btn btn-sm btn-outline-dark" />
	</p>
	<hr/>
	<div>
		출력결과<br/>
		<span id="demo"></span>
	</div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>