<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>wmInput.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@900&display=swap" rel="stylesheet">
	<style>
		body {font-family: 'Gowun Batang', serif;}
	</style>
	<script>
		'use strict';
		function fCheck() {
			let receiveId = myform.receiveId.value;
			let title = myform.title.value;
			let content = myform.content.value;
			
			if(receiveId == "") {
				alert("받는이 아이디를 입력하세요.");
				myform.receiveId.focus();
			}
			else if(title == "") {
				alert("제목을 입력하세요.");
				myform.title.focus();
			}
			else if(content == "") {
				alert("내용을 입력하세요.");
				myform.content.focus();
			}
			else {
				myform.submit();
			}
		}
	</script>
</head>
<body>
<p><br/></p>
<!-- 
	mSw 	=> 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
	mFlag => 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
 -->
<div class="container">
	<form name="myform" method="post" action="${ctp}/webMessage/wmInput">
	<table class="table table-bordered">
		<tr>
			<th>보낸이</th>
			<td><input type="text" name="sendId" value="${sMid}" readonly class="form-control" /></td>
		</tr>
		<tr>
			<th>받는이</th>
			<td><input type="text" name="receiveId" value="${param.receiveId}" placeholder="받는사람 ID 입력" class="form-control" /></td>
		</tr>
		<tr>
			<th>메세지 제목</th>
			<td><input type="text" name="title" placeholder="메세지 제목을 입력하세요" class="form-control" /></td>
		</tr>
		<tr>
			<th>메세지 내용</th>
			<td><textarea rows="5" name="content" class="form-control"></textarea></td>
		</tr>
		<tr>
			<td colspan="2" class="text-center">
				<input type="button" value="메세지 전송" onclick="fCheck()" class="btn btn-sm btn-outline-primary" /> &nbsp;
				<input type="reset" value="다시쓰기" class="btn btn-sm btn-outline-warning" /> &nbsp;
				<input type="button" value="돌아가기" onclick="location.href='${ctp}/webMessage/webMessage';" class="btn btn-sm btn-outline-dark" />
			</td>
		</tr>
	</table>
	</form>
</div>
<p></p>
</body>
</html>