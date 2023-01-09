<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<%
	String email = request.getParameter("email") == null ? "" : request.getParameter("email");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>guestInput.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container w3-border w3-border-red w3-pale-red w3-round-large">
	<form name="myform" method="post" class="was-validated">
		<h2>방명록 글 작성</h2>
		<br/>
		<div class="form-group">
      <label for="name">성명</label>
      <input type="text" class="form-control" name="name" id="name" placeholder="성명을 입력하세요." required />
      <div class="valid-feedback">확인</div>
      <div class="invalid-feedback">성명을 입력하세요.</div>
    </div>
		<div class="form-group">
      <label for="name">전자우편(E-Mail)</label>
      <input type="email" class="form-control" name="email" id="email" placeholder="abc@abc.com" />
    </div>
		<div class="form-group">
      <label for="homePage">홈페이지(블로그)</label>
      <input type="text" class="form-control" name="homePage" id="homePage" value="http://" placeholder="efg@blog.go" />
    </div>
		<div class="form-group">
      <label for="content">방문소감</label>
      <textarea rows="5" class="form-control" name="content" id="content" required></textarea>
      <!-- textarea에서 form-control 을 작성했다면, rows 뒤에 colspan="숫자" 작성 XXX -->
      <div class="valid-feedback">확인</div>
      <div class="invalid-feedback">방문소감 입력은 필수입니다.</div>
    </div>
    <div class="form-group">
	    <button type="submit" class="btn btn-outline-danger">등록</button>
	    <!-- <button type="button" onclick="regexCheck()" class="btn btn-outline-primary">등록</button> -->
	    <!-- 버튼은 submit 말고 프론트 체크 후 되도록 button type으로 유효성 검사하기 -->
	    <button type="reset" class="btn btn-outline-dark">재입력</button>
	    <button type="button" onclick="location.href='${ctp}/guest/guestList';" class="btn btn-outline-secondary">돌아가기</button>
    </div>
    
    <input type="hidden" name="hostIp" id="hostIp" value="<%=request.getRemoteAddr()%>" />
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>