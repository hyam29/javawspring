<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>mailForm.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<style>
		text-aling: center;
	</style>
	<script>
		'use strict';
		function jusorokView() {
			$("#myModal").on("show.bs.modal", function(e) {
				$(".modal-header #cnt").html(${cnt});
				let jusorok = '';
				jusorok += '<table class="table table-hover"';
				jusorok += '<tr class="table-dark text-dark text-center>"';
				jusorok += '<th>번호</th><th>ID</th><th>성명</th><th colspan="2">이메일</th>';
				jusorok += '</tr>';
				jusorok += '<c:forEach var="vo" items="${vos}" varStatus="st">';
				jusorok += '<tr onclick="location.href=\'${ctp}/study/mail/mailForm?email=${vo.email}\';" class="text-center">';
				jusorok += '<td>${st.count}</td>';
				jusorok += '<td>${vo.mid}</td>';
				jusorok += '<td>${vo.name}</td>';
				jusorok += '<td>${vo.email}</td>';
				jusorok += '</tr>';
				jusorok += '</c:forEach>';
				jusorok += '';
				jusorok += '</table>';
				$(".modal-body #jusorok").html(jusorok);
				/* 컨트롤러에서 편집해서 온다면, ${jusorok} 써도 되지만, view 부분이므로 여기서 편집*/
			});
		}
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container w3-border w3-border-yellow w3-pale-yellow w3-round-large" style="opacity:0.8">
	<h2 class="text-center">메일 보내기</h2>
	<p style="color:salmon">※ 받는 사람의 메일주소를 정확히 입력하셔야 합니다.</p>
	<form name="myform" method="post">
		<table class="table w3-border-yellow ">
			<tr>
				<th>받는이 메일</th>			
				<td>
					<div class="row">
						<div class="col-10"><input type="text" name="toMail" value="${email}" placeholder="받는 사람의 메일주소를 입력하세요." class="form-control" autofocus required /></div>
						<div class="col-2"><input type="button" value="주소록" onclick="jusorokView()" class="btn btn-outline-dark form-control" data-toggle="modal" data-target="#myModal" /></div>
					</div>
				</td>			
			</tr>
			<tr>
				<th>제목</th>			
				<td><input type="text" name="title" placeholder="메일 제목을 입력하세요." class="form-control" required /></td>			
			</tr>
			<tr>
				<th>내용</th>			
				<td><textarea rows="7" name="content" class="form-control" required></textarea></td>			
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<input type="submit" value="메일전송" class="btn btn-outline-primary"/>
					<input type="reset" value="재작성" class="btn btn-outline-warning"/>
					<input type="button" value="돌아가기" onclick="location.href='${ctp}/member/memberMain';" class="btn btn-outline-dark"/>
				</td>
			</tr>
		</table>
	</form>
</div>

<!-- 주소록 Modal 출력 시작 -->
<div class="modal pade" id="myModal" style="width:700px">
	<div class="modal-dailog">
		<div class="modal-content" style="width:600px">
			<div class="modal-header" style="width:600px">
				<h4 class="modal-title">◈ 주 소 록 ◈(건수:<span id="cnt"></span>)</h4>
				<button type="button" class="close" data-dissmiss="modal">&times;</button>
			</div>
			<div class="modal-body" style="width:600px; heigth:400px; overflow:auto;">
				<span id="jusorok"></span>
			</div>
			<div class="modal-footer" style="width:600px">
				<button type="button" class="close btn btn-outline-dark" data-dissmiss="modal" >close</button>
			</div>
		</div>
	</div>
</div>
<!-- 주소록 Modal 끝 -->

<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>