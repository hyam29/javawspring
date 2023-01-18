<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>kakaoEx1.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		function addressCheck(latitude, longitude) {
			// alert(latitude + "/" + longitude);
			var address = myform.address.value;
			if(address.trim == "") {
				alert("선택한 지점의 장소명을 입력하세요.");
				myform.address.focus();
				return false;
			}
			
			var query = {
					address : address,
					latitude : latitude,
					longitude : longitude
			}
			
			$.ajax({
				type : "post",
				url : "${ctp}/study/kakaomap/kakaoEx1",
				data : query,
				success : function(res) {
					if(res == "1") alert("선택한 지점이 등록되었습니다.");
					else alert("동일한 명칭이 등록되어있습니다. 다른 이름으로 등록 가능합니다.");
				},
				error : function() {
					alert("지점 등록 전송오류");
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
	<h2>클릭한 위치에 마커 표시</h2>
	<hr/>
	<div id="map" style="width:100%;height:400px;"></div>
	<p><b>마커를 표시할 지도의 위치를 클릭해주세요.</b></p>
	
	<form name="myform">
		<div id="clickPoint"></div>
	</form>
	
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=66e2f4e3f7713daedb82d1c39ec80b47"></script>
	<script>
		var container = document.getElementById('map'); // 지도를 표히나 div 태그 id
		var options = {
			center: new kakao.maps.LatLng(36.63511525023068, 127.4595324289283), // 지도의 중심좌표
			level: 2 // 지도의 확대 레벨 (숫자가 커질수록 축소됨)
		};

		// 지도를 표시할 div의 id와 지도 옵션으로 지도를 생성한다.
		var map = new kakao.maps.Map(container, options);
		
		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
				// 지도 중심좌표에 마커를 생성한다.
		    position: map.getCenter() 
		});

		// 마커가 지도 위에 표시되도록 설정합니다
		marker.setMap(map);
		
		// 지도에 클릭 이벤트를 등록합니다
		// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
		kakao.maps.event.addListener(map, 'click', function(mouseEvent) {        
		    
	    // 클릭한 위도, 경도 정보를 가져옵니다 
	    var latlng = mouseEvent.latLng; 
	    
	    // 마커 위치를 클릭한 위치로 옮깁니다
	    marker.setPosition(latlng);
	    
	    var message = '클릭한 위치의 위도는 <font color="red">' + latlng.getLat() + '</font> 이고, ';
	    message += '경도는 <font color="red">' + latlng.getLng() + '</font> 입니다. &nbsp;';
	    message += '<input type="button" value="위치복귀" onclick="location.reload();" /><br/>';
	    message += '<p>선택한 지점의 장소명 : <input type="text" name="address" /> &nbsp;';
	    // latlng.getLat(), latlng.getLng() : 각각 위도, 경도 불러오는 명령어
	    message += '<input type="button" value="장소저장" onclick="addressCheck('+latlng.getLat()+', '+latlng.getLng()+')" /> </p>';
	    
	    
	   	// var resultDiv = document.getElementById('clickLatlng'); 
	    // resultDiv.innerHTML = message;
		  
		  document.getElementById("clickPoint").innerHTML = message;
		});
	</script>
	<hr/>
	<jsp:include page="kakaoMenu.jsp" />
	<hr/>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>