<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>memberLogin.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="container" style="padding:30px">
				<form name="myform" method="post" class="was-validated">
					<h2>로그인</h2>
					<div class="form-group">
			      <label for="mid">아이디(ID)</label>
			      <input type="text" class="form-control" name="mid" id="mid" value="${mid}" placeholder="아이디를 입력해주세요." autofocus required />
			      <div class="valid-feedback">확인</div>
			      <div class="invalid-feedback">아이디는 필수 입력사항입니다.</div>
			    </div>
					<div class="form-group">
			      <label for="pwd">비밀번호(PASSWORD)</label>
			      <input type="password" class="form-control" name="pwd" id="pwd" placeholder="비밀번호를 입력해주세요." required />
			      <div class="invalid-feedback">비밀번호는 필수 입력사항입니다.</div>
			    </div>
			    <div class="form-group">
				    <button type="submit" class="btn btn-outline-success">로그인(LOGIN)</button>
				    <!-- <button type="button" onclick="regexCheck()" class="btn btn-outline-primary">로그인</button> -->
				    <!-- 버튼은 submit 말고 프론트 체크 후 되도록 button type으로 유효성 검사하기 -->
				    <button type="reset" class="btn btn-outline-warning">재입력</button>
				    <button type="button" onclick="location.href='${ctp}/';" class="btn btn-outline-secondary">메인화면</button>
				    <button type="button" onclick="location.href='${ctp}/member/memberJoin';" class="btn btn-outline-primary">회원가입</button>
			    </div>
			    <div class="row" style="font-size:14px">
			    	<span class="col"><input type="checkbox" name="idCheck" checked />아이디 저장</span>
			    	<span class="col">
			    		[<a href="${ctp}/member/memberIdSearch">아이디 찾기</a>]
			    		[<a href="${ctp}/member/memberPwdSearch">비밀번호 찾기</a>]
			  		</span>
			    </div>
			  </form>
		  </div>
  	</div>
  </div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>