<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <title>schedule.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <style>
  	#td1, #td8, #td15, #td22, #td29, #td36 {color:red}
  	#td7, #td14, #td21, #td28, #td35 {color:blue}
  	
  	.today {
  		background-color: lightblue;
  		color: #fff;
  		font-weight: bolder;
  	}
  	
  	td {
  		text-align: left;
  		vertical-align: top
  	}
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <div class="text-center">
  	<button type="button" onclick="location.href='${ctp}/schedule/schedule?yy=${yy-1}&mm=${mm}';" class="btn btn-outline-secondary btn-sm" title="이전년도">◁◁</button> 
  	<button type="button" onclick="location.href='${ctp}/schedule/schedule?yy=${yy}&mm=${mm-1}';" class="btn btn-outline-secondary btn-sm" title="이전월">◀</button> 
    <font size="5">${yy}년 ${mm+1}월</font>
  	<button type="button" onclick="location.href='${ctp}/schedule/schedule?yy=${yy}&mm=${mm+1}';" class="btn btn-outline-secondary btn-sm" title="다음월">▶</button> 
  	<button type="button" onclick="location.href='${ctp}/schedule/schedule?yy=${yy+1}&mm=${mm}';" class="btn btn-outline-secondary btn-sm" title="다음년도">▷▷</button> 
  	<button type="button" onclick="location.href='${ctp}/schedule/schedule';" class="btn btn-outline-secondary btn-sm" title="오늘날짜"><i class="fa fa-home"></i></button> 
  </div>
  <br/>
  <div class="text-center">
    <table class="table table-bordered" style="height:450px">
      <tr class="text-center" style="background-color:#eee">
        <th style="width:13%; color:red; vertical-align:middle">일</th>
        <th style="width:13%; vertical-align:middle">월</th>
        <th style="width:13%; vertical-align:middle">화</th>
        <th style="width:13%; vertical-align:middle">수</th>
        <th style="width:13%; vertical-align:middle">목</th>
        <th style="width:13%; vertical-align:middle">금</th>
        <th style="width:13%; color:blue; vertical-align:middle">토</th>
      </tr>
      <tr>
      	<c:set var="cnt" value="${1}" />
      	<!-- 시작일 이전의 공백을 전달의 날짜로 채워준다. -->
      	<c:forEach var="preDay" begin="${preLastDay-(startWeek-2)}" end="${preLastDay}">
      		<td style="font-size:0.6em; opacity:0.4">${prevYear}-${prevMonth+1}-${preDay}</td>
					<c:set var="cnt" value="${cnt+1}" />
      	</c:forEach>
      	
      	<!-- 뷰의 오늘날짜와 저장소의 일자가 일치하면 today 클래스 스타일 적용 -->
      	<c:forEach begin="1" end="${lastDay}" varStatus="st">
      		<c:set var="todaySw" value="${toYear == yy && toMonth == mm && toDay == st.count ? 1 : 0}" />
      		<td id="td${cnt}" ${todaySw == 1 ? 'class=today' : ''} style="font-size:0.9em">
      			<!-- value에 한꺼번에 작성하게 되면, int타입이라 연산이 되기 때문에 각각 작성 -->
      			<c:set var="ymd" value="${yy}-${mm+1}-${st.count}" />
      			<a href="scheduleMenu?ymd=${ymd}">
	    				${st.count}<br/>
	    				
	    				<!-- 해당 날짜에 일정이 있으면, part를 출력시키기 위한 변수 선언 -->
	    				<c:set var="tempPart" value="" />
            	<c:set var="tempCnt" value="0" />
            	<c:set var="tempSw" value="0" />
	    				
      				<c:forEach var="vo" items="${vos}">
      					 <c:if test="${fn:substring(vo.SDate,8,10) == st.count}"> <!-- 날짜가 같아? OO -> 아래로 -->
      						<c:if test="${vo.part != tempPart}"> <!-- DB와 동일한 part가 아니라면, -->
      							<c:if test="${tempSw != 0}"> <!-- tempSw도 0이 아니라면, 신규 part -->
				      				- ${tempPart}(${tempCnt})<br/> <!-- DB 데이터 출력 -->
				      				<c:set var="tempCnt" value="0" /> <!-- tampCnt: 0으로 다시 변경 -->
			      				</c:if>
	      						<c:set var="tempPart" value="${vo.part}" />
    							</c:if>
    							<c:set var="tempSw" value="1" /> 
      						<c:set var="tempCnt" value="${tempCnt+1}" /> <!-- 동일파트가 들어오면 sw는 1 유지에 여기로 옴 -->
  							</c:if>	
    					</c:forEach>
    					<c:if test="${tempCnt != 0}">- ${tempPart}(${tempCnt})</c:if> <!-- 제일 첫 데이터는 여기로 바로오게 됨 -->
      			</a>
      		</td>
      		<!-- 한 주가 꽉 차면 줄바꾸기 처리하는 부분 -->
      		<c:if test="${cnt % 7 == 0}"></tr><tr></c:if>
      		<c:set var="cnt" value="${cnt+1}" />
      	</c:forEach>
      	
      	 <!-- 1. 마지막일 이후를 다음달의 1일부터 채워줌 -->
        <c:if test="${nextStartWeek != 1}">
	        <c:forEach begin="${nextStartWeek}" end="7" varStatus="nextDay">
          <td style="opacity:0.4; font-size:0.6em">${nextYear}-${nextMonth+1}-${nextDay.count}</td>
        	</c:forEach>
      	</c:if>
      </tr>
    </table>
  </div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>