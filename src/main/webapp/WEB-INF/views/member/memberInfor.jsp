<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>memberInfor.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
    'use strict';
    
    function midSearch() {
      let mid = myform.mid.value;
      if(mid.trim() == "") {
    	  alert("아이디를 입력하세요!");
    	  myform.mid.focus();
      }
      else {
    	  myform.submit();
      }
    }
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h2>회원정보 상세 조회</h2>
	<hr/>
	<br/>
	<table class="table table-hover">
		<tr><td>아이디: ${vo.mid}</td></tr>
		<tr><td>별명: ${vo.nickName}</td></tr>
		<tr><td>성명: ${vo.name}</td></tr>
		<tr><td>성별: ${vo.gender}</td></tr>
		<tr><td>생일: ${fn:substring(vo.birthday,0,10)}</td></tr>
		<tr><td>전화번호: ${vo.tel}</td></tr>
		<tr><td>주소: ${vo.address}</td></tr>
		<tr><td>이메일: ${vo.email}</td></tr>
		<tr><td>홈페이지: ${vo.homePage}</td></tr>
		<tr><td>직업: ${vo.job}</td></tr>
		<tr><td>취미: ${vo.hobby}</td></tr>
		<tr><td>자기소개: ${vo.content}</td></tr>
		<tr><td>등급: ${sStrLevel}</td></tr>
		<tr><td>총 방문 횟수: ${vo.visitCnt}</td></tr>
		<tr><td>가입일: ${fn:substring(vo.startDate,0,16)}</td></tr>
		<tr><td>최종 접속일: ${fn:substring(vo.lastDate,0,16)}</td></tr>
		<tr><td>오늘 방문 횟수: ${vo.todayCnt}</td></tr>
		<c:if test="${sLevel == 0}">
			<tr><td><font color="red"><b>회원 보유 포인트: ${vo.point}</b></font></td></tr>
			<tr><td><font color="red"><b>정보 공개 여부: ${vo.userInfor}</b></font></td></tr>
			<tr><td>
				<c:if test="${vo.userDel == 'OK'}"><font color="red"></c:if><b>탈퇴신청 여부: ${vo.userDel}</b></font>
			</td></tr>
		</c:if>
		<tr><td>사진: <img src="${ctp}/member/${vo.photo}" width="150px" /></td></tr>
		<!-- memList.mem 에 pag로 다시 돌아가기 위해서 location 작성 -->
		<tr><td class="text-center"><button type="button" class="btn btn-secondary" onclick="location.href='${ctp}/member/memberList?pag=${pag}';">돌아가기</button></td></tr>
	</table>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>