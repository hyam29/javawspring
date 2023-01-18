<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>kakaoEx5.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		var address = '${vo.address}';
		var latitude = '${vo.latitude}';
		var longitude = '${vo.longitude}';
		// alert("address : " + address + ", latitude : " + latitude + ", longitude : " + longitude);
		
		
		function addressSearch() {
			address = myform.address.value;
			if(address.trim == "") {
				alert("검색할 지점을 선택하세요.");
				return false;
			}
			location.href = "${ctp}/study/kakaomap/kakaoEx3?address="+address;
		}
		
		function addressDelete() {
			address = myform.address.value;
			var ans = confirm("해당 지역을 삭제하시겠습니까?");
			if(!ans) return false;
			$.ajax({
				type : "post",
				url : "${ctp}/study/kakaomap/kakaoEx2Delete",
				data : {address : address},
				success : function(res) {
					alert("선택하신 지역이 삭제되었습니다.");
					// location.reload(); 사창사거리(디폴트값)를 지웠을 때, 지역이 없으면 에러... 다시 불러야 함.
					location.href="${ctp}/study/kakaomap/kakaoEx3";
				},
				error : function() {
					alert("삭제 전송오류");
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
	<h2>거리 비교하기</h2>
	<hr/>
	<div id="map" style="width:100%;height:400px;"></div>
	<hr/>
	<jsp:include page="kakaoMenu.jsp" />
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=66e2f4e3f7713daedb82d1c39ec80b47"></script>
	<script>
		var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = { 
	        center: new kakao.maps.LatLng(36.63511525023068, 127.4595324289283), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };
	
		var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	
		<c:forEach var="vo" items="${vos}">
			// 마커가 표시될 위치입니다 
			var markerPosition  = new kakao.maps.LatLng(${vo.latitude}, ${vo.longitude}); 
		
			// 마커를 생성합니다
			var marker = new kakao.maps.Marker({
			    position: markerPosition
			});
		
			// 마커가 지도 위에 표시되도록 설정합니다
			marker.setMap(map);
		</c:forEach>
		
			// 아래 코드는 지도 위의 마커를 제거하는 코드입니다
			// marker.setMap(null);   
	</script>
</div>
<p><br/></p> 
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>