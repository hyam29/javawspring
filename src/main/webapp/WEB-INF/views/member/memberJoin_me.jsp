<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>memJoin.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <!-- 12~13행 : 주소록 API 사용하기 위해 추가 -->
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
  <script src="${ctp}/js/woo.js"></script>
  <script>
	  'use strict';
	  let idCheckSw = 0;
	  let nickCheckSw = 0;
	  
	  // 회원가입폼 체크 후 서버 전송(submit) -> 프론트 유효성 검사
    function fCheck() {
			/* let joinMyForm = document.myform; */
			
			let mid = myform.mid.value;
			let pwd = myform.pwd.value;
			let nickName = myform.nickName.value;
			let name = myform.name.value;
			/* let tel2 = document.getElementById("tel2").value;
			let tel3 = document.getElementById("tel3").value;
			let email = document.getElementById("email").value; */
			  
			let regMid = /[a-zA-Z0-9_]/g; 
			let regName = /[A-Za-z가-힣]{2,20}/g;
			let regTel = /(\d{2,3})-(\d{3,4})-(\d{4})/g;
			let regEmail = /[a-zA-Z0-9_.]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,3}/g;
			
			if(mid.trim() == "") {
			  alert("아이디를 입력해주세요.")
			  document.getElementById("mid").focus();
			  return;
			}
			else if(!regMid.test(mid)) {
			  alert("아이디형식은 영문 대/소문자와 숫자 _만 가능합니다!")
			  document.getElementById("mid").focus();
			  return;
			}
			else if(myform.midDuplication.value != "midCheck") {
				alert("아이디 중복체크를 해주세요!")
			  document.getElementById("mid").focus();
				return;				
			}
			if(pwd.trim() == "") {
			  alert("비밀번호를 입력해주세요.")
			  document.getElementById("pwd").focus();
			  return;
			}
			if(nickName.trim() == "") {
			  alert("닉네임(별명)을 입력해주세요.")
			  document.getElementById("nickName").focus();
			  return;
			}
			else if(myform.nickDuplication.value != "nickCheck") {
				alert("닉네임(별명) 중복체크를 해주세요!")
			  document.getElementById("nickName").focus();
				return;				
			}
			if(name.trim() == "") {
			  alert("성명을 입력해주세요.")
			  document.getElementById("name").focus();
			  return;
			}
			if(!regName.test(name)) {
			  alert("성명은 길이 2~20자의 한글, 영문만 입력가능합니다!")
			  document.getElementById("name").focus();
			  return;
			}
/* 			if(phone.trim() == "") {
			  alert("전화번호를 입력해주세요!")
			  return;
			}
			if(!regTel.test(phone)) {
			  alert("전화번호 형식은 '02(0)-123(4)-5678' 입니다!")
			  document.getElementById("phone").focus();
			  return;
			}
			if(email.trim() == "") {
			  alert("이메일을 입력해주세요!")
			  return;
			}
			if(!regEmail.test(email)) {
			  alert("이메일 형식이 올바르지 않습니다!")
			  document.getElementById("email").focus();
			  return;
			} */
			
			
			// form 에서 따로 입력하는 부분들을 한꺼번에 합쳐서 DB 저장하는 처리 (전화번호, 주소(밑에 작성함), 이메일 등)
			let tel1 = myform.tel1.value;
			let tel2 = myform.tel2.value;
			let tel3 = myform.tel3.value;
			let tel = tel1 + "-" + tel2 + "-" + tel3; // 값 보내는 코드는 전송 직전에(제일 마지막에) 한꺼번에 작성
			
			
			// 파일에 관한 사항 체크! (파일명이 존재 -> 해당 파일을 넘김! // empty -> 'noimage.jpg'를 넘김!)
			let maxSize = 1024 * 1024 * 1; // 1MByte (1KB = 2의 10승 = 1024) 까지만 허용
			let fName = myform.fName.value;
			let ext = fName.substring(fName.lastIndexOf(".")+1); // 파일명 자르고 확장자만 가져오기
			let uExt = ext.toUpperCase(); // 확장자명 대문자로 변경
			
			// submitFlag 스위칭 사용으로 회원가입 최종 완료 여부 체크
			let submitFlag = 0;
			
			if(fName.trim() == "") {
				myform.photo.value = "noimage"; // 확장자명은 백엔드에서 noimage일 경우 자동으로 noimage.jpg 불러오려고 빼고 작성
				submitFlag = 1;
			}
			
			else {
				/*
					1. 파일 용량(size) 체크 (용량이 크면 서버가 죽음)
					2. 사진파일이니까, 그림파일만(포맷형식) 허용가능해야 함! (포맷형식 : jpg,png,gif)
				*/
				let fileSize = document.getElementById("file").files[0].size; 
				// 무조건 id명으로(value) 값 불러와야 함!!! (name은 X(JS약속임...)) // .size : 사이즈 비교위한 JS 명령어
				
				if(fileSize > maxSize) {
					alert("업로드 파일 크기는 1MByte를 초과할 수 없습니다.");
					return false;
				}
				else if(uExt != "JPG" && uExt != "GIF" && uExt != "PNG" && uExt != "JEPG") {
					alert("파일 확장자는 'JPG/GIF/PNG/JEPG'만 사용 가능합니다.");
					return false;
				}
				else if(fName.indexOf(" ") != -1) { // 파일명에 공백이 포함되어 있어?
					alert("파일명은 공백을 포함할 수 없습니다.");
					return false;
				}
				
				else {
					submitFlag = 1;
				}
			}
			
			// 전송 전 모든 체크 끝났다면, submitFlag가 1이 되도록 처리 후 서버로 전송!
			if(submitFlag == 1){
				// 아이디, 닉네임 중복체크 버튼 눌렀어? -> 가입 가능
				
				// 전화번호 하나로 묶어 서버 전송
				myform.tel.value = tel;
				
				// 이메일 하나로 묶어서 서버 전송
				let email1 = myform.email1.value;
				let email2 = myform.email2.value;
				myform.email.value = email1 + "@" + email2;
				
				
				// 전송 전 '주소'를 하나로 묶어 전송 처리 (출력시 붙어나올 것 대비해서 공백 추가 ()+ " ")
				let postcode = myform.postcode.value + " ";
				let roadAddress = myform.roadAddress.value + " ";
				let detailAddress = myform.detailAddress.value + " ";
				let extraAddress = myform.extraAddress.value + " ";
				myform.address.value = postcode + "/" +  roadAddress + "/" + detailAddress + "/" + extraAddress;

				/* alert("회원가입 되었습니다. 환영합니다!"); */
				myform.submit();
			}
			else {
				alert("회원가입에 실패하였습니다.");	
			}
	  }
	  
 		// id 중복체크
    function idCheck() {
    	let mid = myform.mid.value;
    	if(mid.trim() == "" || mid.length<4 || mid.length>=20) {
    		alert("아이디를 확인하세요!(아이디는 4~20자 이내)");
    		myform.mid.focus();
    		return false;
    	}
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/member/memberIdCheck",
    		data  : {mid : mid},
    		success:function(res) {
    			if(res == "1") {
    				alert("이미 사용중인 아이디 입니다.");
    				$("#mid").focus();
    			}
    			else {
    				alert("사용 가능한 아이디 입니다.");
    				idCheckSw = 1;
    			}
    		},
    		error : function() {
    			alert("ID전송오류!");
    		}
    	});
    }
	  
	  
	  
	  // 닉네임 중복체크
	  function nickCheck() {
		  let nickName = myform.nickName.value;
		  let url = "${ctp}/nickNameCheck.mem?nickName="+nickName;
		  
		  if(nickName.trim() == "") {
			  alert("닉네임(별명)을 입력하세요.");
			  myform.nickName.focus();
		  }
		  else {
			  window.open(url, "nWin", "width=580px, height=250px");
		  }
	  }
	  
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<!-- <form name="myform" method="post" class="was-validated" enctype="multipart/form-data"> -->
	<form name="myform" method="post" class="was-validated">
    <h2>* 회 원 가 입 *</h2>
    <br/>
    <div class="form-group">
      <label for="mid">아이디 &nbsp; &nbsp;<input type="button" value="아이디 중복체크" name="midBtn" id="midBtn" class="btn btn-secondary btn-sm" onclick="idCheck()"/></label>
      <input type="text" class="form-control" name="mid" id="mid" placeholder="아이디를 입력하세요." required autofocus/>
    </div>
    <div class="form-group">
      <label for="pwd">비밀번호</label>
      <input type="password" class="form-control" id="pwd" placeholder="비밀번호를 입력하세요." name="pwd" required />
    </div>
    <div class="form-group">
      <label for="nickName">닉네임 &nbsp; &nbsp;<input type="button" value="닉네임 중복체크" name="nickNameBtn" class="btn btn-secondary btn-sm" onclick="nickCheck()"/></label>
      <input type="text" class="form-control" id="nickName" placeholder="별명을 입력하세요." name="nickName" onkeydown="reNickCheck()" required />
    </div>
    <div class="form-group">
      <label for="name">성명</label>
      <input type="text" class="form-control" id="name" placeholder="성명을 입력하세요." name="name" required />
    </div>
    <div class="form-group">
      <label for="email1">Email address</label>
				<div class="input-group mb-3">
				  <input type="text" class="form-control" placeholder="Email을 입력하세요." id="email1" name="email1" required />
				  <div class="input-group-append">
				    <select name="email2" class="custom-select">
					    <option value="gmail.com" selected>gmail.com</option>
					    <option value="daum.net">daum.net</option>
					    <option value="naver.com">naver.com</option>
					    <option value="nate.com">nate.com</option>
					    <option value="hotmail.com">hotmail.com</option>
					    <option value="yahoo.com">yahoo.com</option>
					  </select>
				  </div>
				</div>
	  </div>
    <div class="form-group">
      <div class="form-check-inline">
        <span class="input-group-text">성별</span> &nbsp; &nbsp;
			  <label class="form-check-label">
			    <input type="radio" class="form-check-input" name="gender" value="남자" checked>남자
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="radio" class="form-check-input" name="gender" value="여자">여자
			  </label>
			</div>
    </div>
    <div class="form-group">
      <label for="birthday">생일</label>
      <!-- datetime now 할 필요없이 value 값 넣으면 오늘날짜로 자동으로 달력에 뜸(JAVA명령) -->
      <input type="date" name="birthday" value="<%=java.time.LocalDate.now() %>" class="form-control"/>
    </div>
    <div class="form-group">
      <div class="input-group mb-3">
	      <div class="input-group-prepend">
	        <span class="input-group-text">전화번호</span> &nbsp;&nbsp;
			      <select name="tel1" class="custom-select">
					    <option value="010" selected>010</option>
					    <option value="02">서울</option>
					    <option value="031">경기</option>
					    <option value="032">인천</option>
					    <option value="041">충남</option>
					    <option value="042">대전</option>
					    <option value="043">충북</option>
			        <option value="051">부산</option>
			        <option value="052">울산</option>
			        <option value="061">전북</option>
			        <option value="062">광주</option>
					  </select>-
	      </div>
	      <input type="text" name="tel2" size=4 maxlength=4 class="form-control"/>-
	      <input type="text" name="tel3" size=4 maxlength=4 class="form-control"/>
	    </div> 
    </div>
    <div class="form-group">
      <label for="address">주소</label>
			<input type="hidden" name="address" id="address">
			<div class="input-group mb-1">
				<input type="text" name="postcode" id="sample6_postcode" placeholder="우편번호" class="form-control">
				<div class="input-group-append">
					<input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기" class="btn btn-secondary">
				</div>
			</div>
			<input type="text" name="roadAddress" id="sample6_address" size="50" placeholder="주소" class="form-control mb-1">
			<div class="input-group mb-1">
				<input type="text" name="detailAddress" id="sample6_detailAddress" placeholder="상세주소" class="form-control"> &nbsp;&nbsp;
				<div class="input-group-append">
					<input type="text" name="extraAddress" id="sample6_extraAddress" placeholder="참고항목" class="form-control">
				</div>
			</div>
    </div>
    <div class="form-group">
	    <label for="homepage">Homepage address</label>
	    <input type="text" class="form-control" name="homePage" value="http://" placeholder="홈페이지를 입력하세요." id="homePage"/>
	  </div>
    <div class="form-group">
      <label for="name">직업</label>
      <select class="form-control" id="job" name="job">
        <option>학생</option>
        <option>회사원</option>
        <option>공무원</option>
        <option>군인</option>
        <option>의사</option>
        <option>법조인</option>
        <option>세무인</option>
        <option>자영업</option>
        <option>기타</option>
      </select>
    </div>
    <div class="form-group">
      <div class="form-check-inline">
        <span class="input-group-text">취미</span> &nbsp; &nbsp;
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="등산" name="hobby"/>등산
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="낚시" name="hobby"/>낚시
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="수영" name="hobby"/>수영
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="독서" name="hobby"/>독서
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="영화감상" name="hobby"/>영화감상
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="바둑" name="hobby"/>바둑
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="축구" name="hobby"/>축구
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="checkbox" class="form-check-input" value="기타" name="hobby" checked/>기타
			  </label>
			</div>
    </div>
    <div class="form-group">
      <label for="content">자기소개</label>
      <textarea rows="5" class="form-control" id="content" name="content" placeholder="자기소개를 입력하세요."></textarea>
    </div>
    <div class="form-group">
      <div class="form-check-inline">
        <span class="input-group-text">정보공개</span>  &nbsp; &nbsp; 
			  <label class="form-check-label">
			    <input type="radio" class="form-check-input" name="userInfor" value="공개" checked/>공개
			  </label>
			</div>
			<div class="form-check-inline">
			  <label class="form-check-label">
			    <input type="radio" class="form-check-input" name="userInfor" value="비공개"/>비공개
			  </label>
			</div>
    </div>
    <div class="form-group">
      회원 사진(파일용량:1MByte이내)
      <input type="file" name="fName" id="file" class="form-control-file border"/>
    </div>
    <button type="button" class="btn btn-outline-success" onclick="fCheck()">회원가입</button> &nbsp;
    <button type="reset" class="btn btn-outline-warning">다시작성</button> &nbsp;
    <button type="button" class="btn btn-outline-secondary" onclick="location.href='${ctp}/member/memberLogin';">회원가입 취소</button>
    
    <!-- 사진 업로드 X -> noimage.jpg 넘기기 위해 hidden input 생성 -->
    <!-- 주소는 주소부분에 hidden type으로 만들어 뒀음 -->
    <input type="hidden" name="photo" />
    <input type="hidden" name="tel" />
    <input type="hidden" name="email" />
    <input type="hidden" name="midDuplication" value="midUnCheck" />
    <input type="hidden" name="nickDuplication" value="nickUnCheck" />
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" /></body>
</html>