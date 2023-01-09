<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ajaxTest2_1.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		/* document.ready 로 작성해도 됨. */
		$(function() {
			$("#dodo").change(function() {
				let dodo = $(this).val();
				if(dodo == "") {
					alert("지역을 선택하세요.");
					return false;
				}
				// alert("선택한 지역은 " + dodo +" 입니다.");
				$.ajax({
					type : "post",
					url : "${ctp}/study/ajax/ajaxTest2_1",
					data : {dodo : dodo},
					success : function(res) {
						//	alert("res : " + res);
						let str = '';
						str += '<option value="">도시선택</option>';
						for(let i=0; i<res.length; i++) {
							if(res[i] == null) break;
							str += '<option>'+res[i]+'</option>';
						}
						$("#city").html(str);
					},
					error : function() {
						alert("전송 오류");
					}
				});
			});
		});
		
		function fCheck() {
			let dodo = $("#dodo").val();
    	let city = $("#city").val();
			
    	if(dodo == "" || city == "") {
				alert("지역을 선택해주세요.");
				return false;
			}
			let str = "선택하신 지역은 " + dodo + ", " + city + "입니다. &nbsp;";
			str += '<input type="button" value="재검색" onclick="location.reload()" class="btn btn-sm btn-info" />';
			$("#demo").html(str);
		}
		
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h2>AJax를 활용한 값의 전달 2 (String배열처리)</h2>
	<hr/>
	<form name="myform">
		<h3>도시를 선택하세요.</h3>
		<!-- !!!아래의 부분은 DB에서 가져와야 하는 부분!!! -->
		<select name="dodo" id="dodo">
			<option value="">지역선택</option>
			<option value="서울">서울</option>
			<option value="경기">경기</option>
			<option value="충북">충북</option>
			<option value="충남">충남</option>
		</select>
		<select name="city" id="city">
			<option value="">도시선택</option>
		</select>
		<input type="button" value="선택" onclick="fCheck()" class="btn btn-outline-primary" /> &nbsp;
		<input type="button" value="돌아가기" onclick="location.href='${ctp}/study/ajax/ajaxMenu';" class="btn btn-outline-dark" />
	</form>
	<hr/>
	<div id="demo"></div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>