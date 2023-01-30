<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>wmContent.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@900&display=swap" rel="stylesheet">
	<style>
		body {font-family: 'Gowun Batang', serif;}
	</style>
</head>
<body>
<p><br/></p>
<!-- 
	mSw 	=> 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
	mFlag => 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
 -->
<div class="container">
	<table class="table table-bordered">
		<tr>
			<th>보낸이</th>
			<td>${vo.sendId}</td>
		</tr>
		<tr>
			<th>받는이</th>
			<td>${vo.receiveId}</td>
		</tr>
		<tr>
			<th>제목</th>
			<td>${vo.title}</td>
		</tr>
		<tr>
			<th>내용</th>
			<td style="width:70%; height:150px; ">${fn:replace(vo.content, newLine, "<br/>")}</td>
		</tr>
<!-- 
	mSw 	=> 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
	mFlag => 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
 -->
		<tr>
			<td colspan="2" class="text-center">
				<c:if test="${param.mFlag == 1 || param.mFlag == 2}">
					<input type="button" value="답장" onclick="location.href='webMessage?mSw=0&receiveId=${vo.sendId}';" class="btn btn-sm btn-outline-primary" /> &nbsp;
				</c:if>
				<c:if test="${param.mFlag == 1}">
					<input type="button" value="휴지통" onclick="location.href='webDeleteCheck?mSw=5&idx=${vo.idx}&mFlag=${param.mFlag}';" class="btn btn-sm btn-outline-danger" /> &nbsp;
				</c:if>
				<%-- <c:if test="${mSw == 1 || mSw == 2 || param.mFlag == 11}"> --%>
					<input type="button" value="돌아가기" onclick="location.href='webMessage?mSw=1';" class="btn btn-sm btn-outline-dark" /> &nbsp;
				<%-- </c:if> --%>
			</td>
		</tr>
	</table>
</div>
<p></p>
</body>
</html>