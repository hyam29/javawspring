<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>memberPwdUpdate.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<form name="myform" method="post" class="was-validated">
		<h2 class="text-center">비밀번호 변경</h2>
		<table class="table table-bordered">
			<tr>
				<th>기존 비밀번호</th>
				<td>
					<input type="password" name="oldPwd" id="oldPwd" autofocus required class="form-control" />
		      <div class="invalid-feedback">기존 비밀번호를 입력하세요.</div>
				</td>
			</tr>
			<tr>
				<th>변경 비밀번호</th>
				<td>
					<input type="password" name="newPwd" id="newPwd" required class="form-control" />
		      <div class="invalid-feedback">변경할 비밀번호를 입력하세요.</div>
				</td>
			</tr>
			<tr>
				<th>변경 비밀번호 재입력</th>
				<td>
					<input type="password" name="rePwd" id="rePwd" required class="form-control" />
		      <div class="invalid-feedback">변경할 비밀번호를 한번 더 입력하세요.</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<input type="submit" value="비밀번호 변경" class="btn btn-outline-success" /> &nbsp;
					<input type="reset" value="다시입력" class="btn btn-outline-success" /> &nbsp;
					<input type="button" value="돌아가기" onclick="location.href='${ctp}/member/memberMain';" class="btn btn-outline-success" />
				</td>
			</tr>
		</table>
		<input type="hidden" name="mid" value="${sMid}" />
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>