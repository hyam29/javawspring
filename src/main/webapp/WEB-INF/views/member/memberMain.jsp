<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>memberMain.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h2>회원 정보 및 설정</h2>
	<hr/>
	<div id="memberInfo1" class="mr-5">
		<p><font color="salmon"><b>${sNickName}</b></font>님 반갑습니다!</p>
		<p>현재 회원님의 등급은 "<font color="lightsalmon"><b>${sStrLevel}</b></font>" 입니다.</p>
		<c:if test="${!empty sImsiPwd}">
		  <hr/>
		  현재 임시비밀번호를 발급하여 메일로 전송처리 하였습니다.<br/>
		  개인정보를 확인하시고 필수입력사항을 기재해 주세요.<br/>
		  <a href="${ctp}/member/memberPwdUpdate" class="btn btn-outline-secondary">비밀번호변경으로이동하기</a>
		  <hr/>
	  </c:if>
		<!-- 아래 4개 DB에서 찾아서 커맨드객체에서 session에 담아서 여기에 뿌려줌 -->
		<p>회원님의 보유 포인트 : ${vo.point}</p>
		<p>최종 접속일 : ${fn:substring(vo.lastDate,0,16)}</p>
		<p>총 방문횟수 : ${vo.visitCnt}</p>
		<p>오늘 방문횟수 : ${vo.todayCnt}</p>
	</div>
	
	<div id="memberInfo2">
    <h3>회원사진</h3>
	  <p><img src="${ctp}/resources/member/${vo.photo}" width="200px" /></p>
  </div>
	
	<hr id="memberInfo3" />
	<h4>활동 내역</h4>
	<p>방명록에 올린 글 수 : <span class="viewCheck">${guestCnt}</span></p>
	<p>게시판에 올린 글 수 : </p>
	<p>자료실에 올린 글 수  : </p>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>