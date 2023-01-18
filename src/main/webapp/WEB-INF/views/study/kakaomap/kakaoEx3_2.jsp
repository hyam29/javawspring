<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>kakaoEx3.jsp</title>
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
	<h2>카카오에서 제공되는 DB사용 -> 원하는 지역 띄우기</h2>
	<hr/>
	<div>
		<form name="myform" method="post">
			<div>
				<font size="4"><b>저장된 지명으로 검색하기</b></font>
				<select name="address" id="address">
					<option value="">지역선택</option>
					<c:forEach var="vo" items="${vos}">
						<option value="${vo.address}" <c:if test="${vo.address == address}">selected</c:if>>${vo.address}</option>
					</c:forEach>
				</select>
				<input type="button" value="지역검색" onclick="addressSearch()" />
				<input type="button" value="검색 지역 DB삭제" onclick="addressDelete()" />
			</div>
			<br/>
				<font size="4"><b>검색 후 마커 클릭 시 DB 저장</b></font>
				<input type="text" name="keyword" id="keyword" size="15" />
				<input type="button" value="검색" onclick="keywordSearch()" />
				<div id="clickPoint"></div>
		</form>
	</div>
	<hr/>
	<div id="map" style="width:100%;height:400px;"></div>
	<hr/>
	<jsp:include page="kakaoMenu.jsp" />
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=66e2f4e3f7713daedb82d1c39ec80b47&libraries=services"></script>
	<script>
		// 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
		var infowindow = new kakao.maps.InfoWindow({zIndex:1});
		
		var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		    mapOption = {
		        center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
		        level: 3 // 지도의 확대 레벨
		    };  
		
		// 지도를 생성합니다    
		var map = new kakao.maps.Map(mapContainer, mapOption); 
		
		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
				// 지도 중심좌표에 마커를 생성한다.
		    position: map.getCenter() 
		});

		// 마커가 지도 위에 표시되도록 설정합니다
		marker.setMap(map);
		
		// 장소 검색 객체를 생성합니다
		var ps = new kakao.maps.services.Places(); 
		
		
		// 키워드로 장소를 검색합니다
		function keywordSearch() {
			var locationSearch = $("#keyword").val();
			ps.keywordSearch(locationSearch, placesSearchCB); 
		}
		
		
		// 키워드 검색 완료 시 호출되는 콜백함수 입니다
		function placesSearchCB (data, status, pagination) {
		    if (status === kakao.maps.services.Status.OK) {
		
		        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
		        // LatLngBounds 객체에 좌표를 추가합니다
		        var bounds = new kakao.maps.LatLngBounds();
		
		        for (var i=0; i<data.length; i++) {
		            displayMarker(data[i]);    
		            bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
		        }       
		
		        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
		        map.setBounds(bounds);
		    } 
		}
		
		// 지도에 마커를 표시하는 함수입니다
		function displayMarker(place) {
		    
		    // 마커를 생성하고 지도에 표시합니다
		    var marker = new kakao.maps.Marker({
		        map: map,
		        position: new kakao.maps.LatLng(place.y, place.x) 
		    });
				
		    
		    // 마커에 클릭이벤트를 등록합니다
		    kakao.maps.event.addListener(marker, 'click', function(mouseEvent) {
		        // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
		        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
		        infowindow.open(map, marker);
		        
		        
		        var query = {
		        		address : place.place_name,
		        		latitude : place.y,
		        		longitude : place.x
		        }
		        
		        $.ajax({
		        	type : "post",
		        	url : "${ctp}/study/kakaomap/kakaoEx3",
		        	data : query,
		        	success : function(res)	{
		        		if(res == "1") {
					        var ans = confirm("선택하신 위치를 DB에 저장하시겠습니까?");
					        if(!ans) return false;
		        			alert("해당 장소 저장이 완료되었습니다.");
		        			location.reload();
		        		}
		        		else alert("해당 장소는 이미 저장되어있습니다.");
		        	},
		        	error : function() {
		        		alert("장소저장 전송오류");
		        	}
		        });
		        
		        
		    });
		    
		    
		}

	</script>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>