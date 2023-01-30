<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>transaction.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		/* 매개변수로 넘겨도 되지만, 아래와 같은 방법도 있음. */
		function fCheck1() {
			myform.action = "${ctp}/study/transaction/input1";
			myform.submit();
		}
		function fCheck2() {
			myform.action = "${ctp}/study/transaction/input2";
			myform.submit();
		}
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<form name="myform" method="post">
		<h2>트랜잭션 연습</h2>
		<p>
			하나의 프로세스 처리과정에서 두개 이상의 자료(작업)을 동시에 처리하고자 할 때, <br/>
			두개 이상의 작업 중 하나라도 처리가 미결된다면 모든 작업은 동시에 초기화되어야 함(원자성) => Roll Back
		</p>
		<hr/>
		<h2>User 등록폼</h2>
		<table class="table table-bordered text-center">
			<tr>
				<th>ID</th>
				<td><input type="text" name="mid" autofocus class="form-control" /></td>
			</tr>
			<tr>
				<th>성명</th>
				<td><input type="text" name="name" class="form-control" /></td>
			</tr>
			<tr>
				<th>나이</th>
				<td><input type="number" name="age" class="form-control" /></td>
			</tr>
			<tr>
				<th>주소</th>
				<td><input type="text" name="address" class="form-control" /></td>
			</tr>
			
			<tr>
				<th>별명</th>
				<td><input type="text" name="nickName" class="form-control" /></td>
			</tr>
			<tr>
				<th>직업</th>
				<td><input type="text" name="job" class="form-control" /></td>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<div class="row">
						<div class="col"><input type="button" value="개별입력" onclick="fCheck1()" class="btn btn-outline-primary" /></div>
						<div class="col"><input type="button" value="일괄입력" onclick="fCheck2()" class="btn btn-outline-info" /></div>
						<div class="col"><input type="reset" value="재입력" class="btn btn-outline-warning" /></div>
						<div class="col"><input type="button" value="리스트보기" onclick="location.href='transactionList';" class="btn btn-outline-dark" /></div>
					</div>
				</td>
			</tr>
			<tr><td colspan="2" class="m-0 p-0"></td></tr>
		</table>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>