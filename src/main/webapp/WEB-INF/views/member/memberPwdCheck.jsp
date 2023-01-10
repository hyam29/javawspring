<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>memberPwdCheck.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	'use strict';
  	function pwdCheck() {
  		let pwd = $("#pwd").val();
  		if(pwd.trim() == "") {
  			alert("비밀번호를 입력해주세요.");
  			$("#pwd").focus();
  			return false;
  		}
  		myform.submit();
  	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <p style="font-size:3em;" class="text-center"><b>비밀번호 확인</b></p>
  <hr/>
  <br/>
  <div class="w3-content" style="max-width:700px">
    <div class="w3-panel w3-leftbar w3-light-grey">
      <p><i>"회원 정보 수정을 위해 비밀번호를 확인합니다."</i></p>
    </div>
    
    <form name="myform" method="post" class="was-validated">
	    <br/>
	    <div class="form-group">
	      아이디 &nbsp; &nbsp;
	      <b><input type="text" class="form-control" value="${sMid}" name="mid" id="mid" readonly/></b>
	    </div>
	    <div class="form-group">
	      <label for="pwd">비밀번호</label>
	      <input type="password" class="form-control" id="pwd" placeholder="비밀번호를 입력하세요." name="pwd" required />
	    </div>
	    <div class="text-center">
	    	<input type="button" value="확인" onclick="pwdCheck()" class="w3-button w3-border-teal w3-border w3-round-large mr-4" />
      	<input type="button" value="돌아가기" onclick="location.href='${ctp}/member/memberMain';" class="w3-button w3-white w3-border w3-round-large" />
	    </div>
	    <input type="hidden" name="deletePwdCheck" />
    </form>
  </div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>