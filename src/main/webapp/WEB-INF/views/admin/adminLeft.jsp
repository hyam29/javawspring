<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>adminLeft.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
</head>
<body style="background-color:#cef">
<p><br/></p>
<div class="container text-center">
  <h5>관리자메뉴</h5>
  <hr/>
  <p>
    <a href="${ctp}/" target="_top">홈으로</a>
  </p>
  <hr/>
  <p>
    <a href="#">방명록리스트</a>
  </p>
  <hr/>
  <p>
    <a href="${ctp}/admin/member/adminMemberList" target="adminContent">회원리스트</a>
  </p>
  <hr/>
  <b>기타작업</b>
  <p>
    <a href="${ctp}/admin/file/fileList" target="adminContent">임시파일관리</a>
  </p>
</div>
<p><br/></p>
</body>
</html>