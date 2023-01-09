<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>message.jsp</title>
	<script>
	'use strict';
	let msg = '${msg}';
	let url = '${ctp}/${url}';
	
	/* message 컨트롤러에서 처리할 거라서, JSP 와 다른 점 */
	alert(msg);
	location.href = url;
	
	</script>
</head>
<body>
</body>
</html>