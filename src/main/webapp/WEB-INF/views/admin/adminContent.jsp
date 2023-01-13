<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>adminContent.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<p><br/></p>
<div class="container">
	<h2>관리자 페이지</h2>
	<hr/>
	<p>
		한달 간 신규 가입자: <b>${monthJoin}명</b>
	</p>
	<hr/>
	<div>
		새로운 글
		<table class="table table-hover">
			<tr>
				<th colspan="3" class="table-dark text-white" width="20%">방명록(Guest)</th>
				<td>g</td>
				<td>g</td>
				<td>g</td>
			</tr>
			<tr>
				<th colspan="3" class="table-dark text-white">게시판(Board)</th>
				<td>g</td>
				<td>g</td>
				<td>g</td>
			</tr>
			<tr>
				<th colspan="3" class="table-dark text-white">자료실(PDS)</th>
				<td>g</td>
				<td>g</td>
				<td>g</td>
			</tr>
			<tr><td colspan="6"></td></tr>
		</table>
	</div>
	<hr/>
	<p>
		방명록, 게시판 등 만들어둬야 함
	</p>
</div>
<p><br/></p>
</body>
</html>