<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>qrCode.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		function qrCreate(no) {
			let mid = '';
			let email = '';
			let query = '';			
			let moveUrl = '';
			
			if(no == 1) {
				mid = myform.mid.value;
				email = myform.email.value;
				query = {
						mid : mid,
						moveFlag : email
				}
			}
			else if(no == 2) {
				moveUrl = myform.moveUrl.value;
				query = {
						moveFlag : moveUrl
				}
			}
			else if(no == 3) {
				mid = myform.mid.value;
				email = myform.email.value;
				let film = myform.film.value;
				let theater = myform.theater.value;
				let time = myform.time.value;
				let ticket = myform.ticket.value;
				query = {
						mid : mid,
						moveFlag : email,
						film : film,
						theater : theater,
						time : time,
						ticket : ticket
				}
			}
			
			$.ajax({
				type : "post",
				url : "${ctp}/study/qrCode",
				data : query,
				success : function(res)	{
					alert("qr코드가 생성되었습니다! 이름 ▶ " + res);
					$("#qrCodeView").show();
					$("#qrView").html(res);
					let qrImage = '<img src="${ctp}/data/qrCode/'+res+'.png" />';
					$("#qrImage").html(qrImage);
				},
				error : function() {
					alert("qr코드 전송 오류");
				}
			});
		}
		
		function qrCheck() {
			let idxSearch = $("#idxSearch").val();
			$.ajax({
				type : "post",
				url : "${ctp}/study/qrSearch",
				data : {idxSearch : idxSearch},
				success : function(res) {
					if(res != "") {
						$("#qrBigo").html(res);
					}
					else {
						$("#qrBigo").text("검색하신 고유번호에 해당되는 정보가 없습니다.");
					}
				},
				error : function() {
					alert("qr비고보기 전송 실패");
				}
			});
		}
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<form name="myform">
		<h2>QR Code 생성연습</h2>
		<p>
			<b>개인정보 입력</b> : <br/>
			아이디 : <input type="text" name="mid" value="${vo.mid}" class="mb-2" /><br/>
			이메일 : <input type="text" name="email" value="${vo.email}" class="mb-2" /><br/>
			<input type="button" value="신상정보QR생성" onclick="qrCreate(1)" class="btn btn-sm btn-outline-success" />
		</p>
		<hr/>
		<h4>소개하고 싶은 사이트 주소를 입력하세요.</h4>
		<p>
			<!-- value 값에 '/' 넣으면 안됨 (파일명에 / 들어가면 안되기 때문) -->
			이동할 주소 : <input type="text" name="moveUrl" value="daum.net" size="40" />
									<input type="button" value="소개QR생성" onclick="qrCreate(2)" class="btn btn-sm btn-outline-primary" />
		</p>
		<hr/>
		<h4>예약 확인 고유번호 생성</h4>
		<p>
			<select id="film">
				<option value="">영화선택</option>
				<option value="아바타">아바타</option>
				<option value="더글로리">더글로리</option>
				<option value="유령">유령</option>
			</select>
			<select id="theater">
				<option value="">관 선택</option>
				<option value="1관">1관</option>
				<option value="2관">2관</option>
				<option value="3관">3관</option>
			</select>
			<select id="time">
				<option value="">시간</option>
				<option value="9시">9시</option>
				<option value="13시">13시</option>
				<option value="18시">18시</option>
			</select>
			<input type="number" name="ticket" min="1" />
			<input type="button" value="예매QR생성" onclick="qrCreate(3)" class="btn btn-sm btn-outline-info" />
		</p>
		<hr/>
		<div id="qrCodeView" style="display:none">
			<h3>생성된 QR코드 확인하기</h3>
			<div>
				- 생성된 qr코드명 : <span id="qrView"></span><br/>
				<span id="qrImage"></span>
			</div>
		</div>
		<div>
			<h3>예약확인(비고)</h3>
			<input type="text" name="idxSearch" id="idxSearch" />
			<input type="button" value="예약번호조회" onclick="qrCheck()" class="btn btn-sm btn-outline-warning" />
			<span id="qrBigo"></span>
		</div>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>