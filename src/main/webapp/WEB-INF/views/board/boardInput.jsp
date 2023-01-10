<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>boardInput.jsp</title>
  <script src="${ctp}/ckeditor/ckeditor.js"></script>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <style>
  	th {
  		text-align: center;
  		background-color: lightyellow;
  		width: 20%;
  	}
  </style>
  <script>
  	'use strict';
  	function fCheck() {
  		let title = myform.title.value;
  		let content = myform.title.value;
  		
  		if(title.trim() == "") {
  			alert("글 제목을 작성해주세요.");
  			myform.title.focus();
  		}
  		/* else if(content.trim() == "") {
  			alert("글 내용을 입력해주세요.");
  			myform.content.focus();
  		} */
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
	<form name="myform" method="post">
		<h2 class="text-center">게시판 글 작성</h2>	
		<br/>
		<table class="table table-bordered">
			<tr>
				<th>글쓴이</th>
				<td>${sNickName}</td>
			</tr>
			<tr>
				<th>글제목</th>
				<td><input type="text" name="title" id="title" placeholder="제목을 입력하세요." autofocus required class="form-control" /></td>
			</tr>
			<tr>
				<th>이메일</th>
				<td><input type="text" name="email" id="email" placeholder="이메일을 입력하세요." value="${email}" class="form-control" /></td>
			</tr>
			<tr>
				<th>홈페이지(블로그)</th>
				<td><input type="text" name="homePage" id="homePage" placeholder="홈페이지(블로그) 주소를 입력하세요." value="${homePage}" class="form-control" /></td>
			</tr>
			<tr>
				<th>글내용</th>
				<td><textarea rows="6" name="content" id="CKEDITOR" class="form-control" required></textarea></td> <!-- BS에서는 textarea col 필요없음! -->
				<!--
					filebrowserUploadUrl(명령어) : 서버에 (그림)파일 업로드 할거야. 파일업로드에 대한 백엔드 처리는 HomeController에서 처리
					uploadUrl(명령어) : 드래그로 여러파일 올릴거야! 경로는 상동! (크기 조절이 안돼서 정렬이 안되므로 미리 사진 크기를 편집해둬야 함)
				-->
				<script>
					CKEDITOR.replace("content", {
						height:500,
						filebrowserUploadUrl:"${ctp}/imageUpload",
						uploadUrl : "${ctp}/imageUpload"
					});
				</script>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<input type="button" value="등록" onclick="fCheck()" class="btn btn-outline-success" /> &nbsp;
					<input type="reset" value="재작성" class="btn btn-outline-warning" /> &nbsp;
					<input type="button" value="작성취소" onclick="location.href='${ctp}/board/boardList?pag=${pag}&pageSize=${pageSize}';" class="btn btn-outline-secondary" /> &nbsp;
				</td>
			</tr>
		</table>
		
		<input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}" />
		<input type="hidden" name="mid" value="${sMid}" />
		<input type="hidden" name="nickName" value="${sNickName}" />
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>