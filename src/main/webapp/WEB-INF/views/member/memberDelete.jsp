<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>memberDelete.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	'use strict';
  	function memberDelCheck() {
  		let pwd = $("#pwd").val();
  		if(pwd.trim() == "") {
  			alert("비밀번호를 입력해주세요.");
  			$("#pwd").focus();
  			return false;
  		}
  		let ans = confirm("회원탈퇴를 하시겠습니까?")
  		if(ans) myform.submit();
  	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <p style="font-size:3em;" class="text-center"><b>회 원 탈 퇴</b></p>
  <hr/>
  <br/>
  <div class="w3-content" style="max-width:700px">
    <h5 class="w3-center w3-padding-64"><span class="w3-tag w3-wide">회원 탈퇴 시 아래 내용을 먼저 확인해주세요.</span></h5>
    <div class="w3-panel w3-leftbar w3-light-grey">
      <p><i>"회원 탈퇴는 신청하시는 즉시 처리됩니다."</i></p>
      <p><i>"회원탈퇴 후 한달 간은 동일한 아이디로 가입할 수 없습니다."</i></p>
      <p><i>"로그인이 필요한 서비스 이용은 더 이상 이용하실 수 없게 됩니다."</i></p>
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
      	<input type="button" value="취소" onclick="location.href='${ctp}/member/memberMain';" class="w3-button w3-border-teal w3-border w3-round-large" />
	    	<input type="button" value="회원탈퇴" onclick="memberDelCheck()" class="w3-button w3-white w3-border w3-round-large mr-4" />
	    </div>
	    <input type="hidden" name="deletePwdCheck" />
    </form>
  </div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>