<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>kakaomap.jsp(기본지도)</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h2>KakaoMap API(기본지도)</h2>
	<hr/>
	<div id="map" style="width:100%;height:400px;"></div>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=66e2f4e3f7713daedb82d1c39ec80b47"></script>
	<script>
		var container = document.getElementById('map'); // 지도를 표히나 div 태그 id
		var options = {
			center: new kakao.maps.LatLng(35.8294683173518, 128.53329160524333), // 지도의 중심좌표
			level: 3 // 지도의 확대 레벨 (숫자가 커질수록 축소됨)
		};

		var map = new kakao.maps.Map(container, options);
	</script>
	<hr/>
	<jsp:include page="kakaoMenu.jsp" />
	<hr/>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>