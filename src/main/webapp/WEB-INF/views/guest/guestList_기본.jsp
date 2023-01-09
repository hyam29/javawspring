<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>guestList.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  
  <style>
  	th {
  		text-align: center;
  		background-color: #aaa;
  	}
  </style>
  
  <script>
		'use strict';
  	function delCheck(idx) {
  		let ans = confirm("삭제 하시겠습니까?");
  		if(ans) location.href="${ctp}/guestDelete.gu?idx="+idx;
  		/* 컨트롤러가 .뒤로는 읽지않으므로, 아무거나 써도 괜찮음! (지워도 됨) */
  	}
  </script>
  
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h2 class="text-center">* 방 명 록 리 스 트 *</h2>
	<br/>

	<table class="table table-borderless mb-0">
		<tr>
			<td>
				<c:if test="${sAMid != 'admin'}"><a href="${ctp}/adminLogin" class="btn btn-sm btn-outline-secondary">관리자</a></c:if>
				<c:if test="${sAMid == 'admin'}"><a href="${ctp}/adminLogout" class="btn btn-sm btn-outline-warning">관리자 로그아웃</a></c:if>
			</td>
			<td style="text-align:right;"><a href="${ctp}/guest/guestInput" class="btn btn-sm btn-outline-info">글쓰기</a></td>
		</tr>
	</table>
	
	<c:set var="no" value="${fn:length(vos)}" />
	 <!-- 방명록 방문번호를 글 삭제해서 번호가 이어지게끔 만들어주기! idx 아닌 no 변수 선언해서 변수를 보여주게끔한 것. forEach 전에 no-1 해주면 번호가 순차적으로 출력됨 -->
	<c:forEach var="vo" items="${vos}" varStatus="st">
		<table class="table table-borderless mb-0">
			<tr>
				<td>방문번호 : ${no}
					<%-- <c:if test="${sAMid == 'admin'}"><a href="${ctp}/guDelete.gu?idx=${vo.idx}" class="btn btn-sm btn-outline-danger">삭제</a></c:if></td> --%>
					<!-- 삭제 재확인을 위해서 javascript 작성으로 위에 script 사용 선언 + delCheck 함수 괄호 안에 꼭 $ { vo.idx}!!! -->					
					<c:if test="${sAMid == 'admin'}"><a href="javascript:delCheck(${vo.idx})" class="btn btn-sm btn-outline-danger">삭제</a></c:if></td>
				<td style="text-align:right;">방문IP : ${vo.hostIp}</td>
			</tr>
		</table>
		<table class="table table-bordered">
			<tr>
				<th style="width:20%;">성명</th>
				<td style="width:25%;">${vo.name}</td>
				<th style="width:20%;">방문일자</th>
				<td style="width:35%;">${fn:substring(vo.visitDate,0,19)}</td>
			</tr>
			<tr>
				<th>전자우편(E-Mail)</th>
				<td colspan="3">
					<c:if test="${fn:length(vo.email) <= 4}"> - 입력사항 없음 - </c:if>
					<c:if test="${fn:length(vo.email) > 4}"><a href="${vo.email}" target="_blank">${vo.email}</a></c:if>
				</td>
			</tr>
			<tr>
				<th>홈페이지(블로그)</th>
				<td colspan="3">
					<c:if test="${fn:length(vo.homePage) <= 8}"> - 입력사항 없음 - </c:if>
					<c:if test="${fn:length(vo.homePage) > 8}"><a href="${vo.homePage}" target="_blank">${vo.homePage}</a></c:if>
				</td>
			</tr>
			<tr>
				<th>방문소감</th>
				<td colspan="3">${fn:replace(vo.content, newLine, '<br/>')}</td>
				<!-- 제일 위에 taglib 지시자 선언 후, replace function 사용 -->
			</tr>
		</table>
		<br/>
		<c:set var="no" value="${no - 1}"></c:set>
	</c:forEach>
	
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>