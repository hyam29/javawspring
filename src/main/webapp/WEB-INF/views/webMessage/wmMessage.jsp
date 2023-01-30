<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>wmMessage.jsp(메세지창 메인화면)</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@900&display=swap" rel="stylesheet">
	<script>
		'use strict';
		function jusorokView() {
			$("#myModal").on("show.bs.modal", function(e) {
				$(".modal-header #cnt").html(${cnt});
	    		let jusorok = '';
	    		jusorok += '<table class="table table-hover">';
	    		jusorok += '<tr class="table-dark text-dark text-center">';
	    		jusorok += '<th>번호</th><th>아이디</th><th>성명</th>';
	    		jusorok += '</tr>';
	    		jusorok += '<c:forEach var="vo" items="${memberVos}" varStatus="st">';
	    		jusorok += '<tr onclick="location.href=\'${ctp}/webMessage/webMessage?mSw=0&receiveId=${vo.mid}\';" class="text-center">';
	    		/* jusorok += '<tr onclick="location.href=\'${ctp}/study/mail/mailForm?email=${vo.email}\';" class="text-center">'; */
	    		jusorok += '<td>${st.count}</td>';
	    		jusorok += '<td>${vo.mid}</td>';
	    		jusorok += '<td>${vo.name}</td>';
	    		jusorok += '</tr>';
	    		jusorok += '</c:forEach>';
	    		jusorok += '';
	    		jusorok += '</table>';
	    		$(".modal-body #jusorok").html(jusorok);
				/* 컨트롤러에서 편집해서 온다면, ${jusorok} 써도 되지만, view 부분이므로 여기서 편집*/
			});
		}
	</script>
	<style>
		body {font-family: 'Gowun Batang', serif;}
	
		#leftWindow {
			float : left;
			width : 20%;
			height : 520px;
			text-align : center;
			background-color: #edf;
			overflow:auto;
		}
		
		#rightWindow {
			float : left;
			width : 75%;
			height : 520px;
			text-align : left;
			background-color: #eef;
			overflow:auto;
		}
		
		/* clear : both; 위치 고정 */
		#footerMargin {clear : both; margin : 5px;}
		
		h3 {text-align : center;}
	</style>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<!-- 
	mSw 	=> 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
	mFlag => 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
 -->
<div class="container">
	<h2 class="text-center"> 메세지 관리 ✿˘◡˘✿ </h2>
	<div class="text-left">(현재접속자:<font color='salmon'><b>${sMid}</b></font>)</div><br/>
	<div id="leftWindow">
		<p><br/></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=0">메세지작성</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=1&mFlag=1">받은메세지</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=2&mFlag=2">새메세지</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=3&mFlag=3">보낸메세지</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=4">수신확인</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=5&mFlag=5">휴지통</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=7">휴지통비우기</a></p>
	</div>
	<div id="rightWindow">
		<p>
			<c:if test="${mSw == 0}">
				<h3>메세지작성</h3>
				<!-- <div class="text-right"><a href="javascript:jusorokView()" class="btn btn-sm btn-outline-primary m-0 mr-3" data-toggle="modal" data-target="#myModal">주소록</a></div> -->
				<div class="text-right"><input type="button" value="주소록" onclick="jusorokView()" class="btn btn-sm btn-outline-dark m-0 mr-3" data-toggle="modal" data-target="#myModal" /></div>
				<jsp:include page="wmInput.jsp" />
			</c:if>
			<c:if test="${mSw == 1}">
				<h3>받은메세지</h3>
				<jsp:include page="wmList.jsp" />
			</c:if>
			<c:if test="${mSw == 2}">
				<h3>새 메세지</h3>
				<jsp:include page="wmList.jsp" />
			</c:if>
			<c:if test="${mSw == 3}">
				<h3>보낸메세지</h3>
				<jsp:include page="wmList.jsp" />
			</c:if>
			<c:if test="${mSw == 4}">
				<h3>수신 확인</h3>
				<jsp:include page="wmList.jsp" />
			</c:if>
			<c:if test="${mSw == 5}">
				<h3>휴 지 통</h3>
				<jsp:include page="wmList.jsp" />
			</c:if>
			<c:if test="${mSw == 6}">
				<h3>메세지 내용보기</h3>
				<jsp:include page="wmContent.jsp" />
			</c:if>
		</p>
	</div>
</div>

<!-- 주소록 Modal 출력 시작 -->
<div class="modal pade" id="myModal" style="width:700px">
	<div class="modal-dailog">
		<div class="modal-content" style="width:600px">
			<div class="modal-header" style="width:600px">
				<h4 class="modal-title">◈ 주 소 록 ◈ (건수:<span id="cnt"></span>)</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body" style="width:600px; heigth:400px; overflow:auto;">
				<span id="jusorok"></span>
			</div>
			<div class="modal-footer" style="width:600px">
				<button type="button" class="close btn btn-dark" data-dismiss="modal" >close</button>
			</div>
		</div>
	</div>
</div>
<!-- 주소록 Modal 끝 -->

<p></p>
<div id="footerMargin">
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</div>
</body>
</html>