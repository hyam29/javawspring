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
    <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
  	<script>
  	'use strict';
  	window.Kakao.init("158c673636c9a17a27b67c95f2c6be5c");
  
    // 카카오 로그인
  	function kakaoLogin() {
  		//로그인 인증하겠다, 기능을적을거라 중괄호
  		window.Kakao.Auth.login({
  			//동의항목에서 닉네임,이메일 변수
  			scope: 'profile_nickname, account_email', 
  			success:function(autoObj) {
				 	//인증과정을거쳐서 토큰값을 보내줌
  				console.log(Kakao.Auth.getAccessToken(),"로그인 OK");
  				console.log(autoObj);
  				window.Kakao.API.request({
  					url : '/v2/user/me',
  					success:function(res) {
  						const kakao_account = res.kakao_account;
  						console.log(kakao_account);
  						// alert(kakao_account.email + " / " + kakao_account.profile.nickname);
  						// 이메일/닉네임 이렇게나옴
  						// KakaoLoginOk만드는건(아래처럼하지말고 ok로 바로보내기) 숙제
  						location.href="${ctp}/member/memberKakaoLogin?nickName="+kakao_account.profile.nickname+"&email="+kakao_account.email;
  					}
  				}); // api쓴다고 요청함! -> ajax와 동일
  			}
  		});
  	}
  	
  	// 카카오 로그아웃
  	function kakaoLogout(kakaoKey) {
  		// 다음에 로그인시에 동의항목 체크하고 로그인할 수 있도록 로그아웃시키기
  		/*
  		//연결끊기
			Kakao.API.request({
	      url: '/v1/user/unlink',
	    })
	    */
  		Kakao.Auth.logout(function() {
  			//토큰이 null값이면 세션이 끊어진거임
  			console.log(Kakao.Auth.getAccessToken(), "토큰 정보가 없습니다.(로그아웃되셨습니다.)");
  		});
  	}
  </script>
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
			    <div class="mb-2">
				    <a href="javascript:kakaoLogin();"><img src="${ctp}/images/kakao_login_medium_narrow.png" /></a>			      
				    <!-- <a href="javascript:kakaoLogout();" class="btn btn-danger">로그아웃</a> -->			      
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