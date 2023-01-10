<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>memberIdSearch.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		function midCheck() {
			let email = $("#email").val();
			if(email.trim() == "") {
				alert("이메일을 입력해주세요.");
			}
			
			$.ajax({
				type : "post",
				url : "${ctp}/member/memberIdSearch",
				data : {email : email},
				success : function(res) {
					if(res == "0") {
						$("#demo").html("찾으시는 아이디가 없습니다. <input type='button' value='회원가입 하러가기' onclick='location.href=\"${ctp}/member/memberJoin\";' class='btn btn-outline-primary' />");
					}
					else {
						$("#demo").html("회원님의 아이디는 <font color='blue'><b>" + res + "</b></font> 입니다.<br/><input type='button' value='로그인' onclick='location.href=\"${ctp}/member/memberLogin\";' class='btn btn-outline-dark mt-2 mr-2' /><input type='button' value='비밀번호 찾기' onclick='location.href=\"${ctp}/member/memberPwdSearch\";' class='btn btn-outline-info mt-2' />");
					}
				},
				error : function() {
					alert("아이디 찾기 전송오류");				
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
	<h2>아이디 찾기</h2>
	<br/>
	<form name="myform" method="post">
		<table class="table table-bordered">
			<tr>
				<th>이메일</th>
				<td><input type="text" name="email" id="email" placeholder="회원가입 시 입력한 이메일을 입력해주세요." class="form-control" /></td>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<input type="button" value="아이디 찾기" onclick="midCheck()" class="btn btn-outline-info" />
					<input type="reset" value="재입력" class="btn btn-outline-warning" />
					<input type="button" value="돌아가기" onclick="location.href='${ctp}/member/memberLogin';" class="btn btn-outline-dark" />
				</td>
			</tr>
		</table>
	</form>
	<br/><hr/>
	<div id="demo" class="text-center"></div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>