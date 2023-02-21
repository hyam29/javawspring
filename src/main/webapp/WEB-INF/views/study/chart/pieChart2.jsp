<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>analysisPieChart.jsp</title>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	
	<script type="text/javascript">
	  //alert("이곳은 pie차트입니다.");
		// Load google charts
		google.charts.load('current', {'packages':['corechart']});
		google.charts.setOnLoadCallback(drawChart);
		
		// Draw the chart and set the chart values
		function drawChart() {
			var pVo = "${pVo.subTitle}".split("/");
			var voteSum1 = Number("${vos.get(0).voteSum}");
			var voteSum2 = Number("${vos.get(1).voteSum}");
			var voteSum3 = Number("${vos.get(2).voteSum}");
			var voteSum4 = Number("${vos.get(3).voteSum}");
			var voteSum5 = Number("${vos.get(4).voteSum}");
			var k1 = pVo[0]+"("+voteSum1+"명)";
			var k2 = pVo[1]+"("+voteSum2+"명)";
			var k3 = pVo[2]+"("+voteSum3+"명)";
			var k4 = pVo[3]+"("+voteSum4+"명)";
			var k5 = pVo[4]+"("+voteSum5+"명)";
			var v1 = voteSum1;
			var v2 = voteSum2;
			var v3 = voteSum3;
			var v4 = voteSum4;
			var v5 = voteSum5;
			
		  var data = google.visualization.arrayToDataTable([
			  ['Task', 'Hours per Day'],
			  [k1, v1],
			  [k2, v2],
			  [k3, v3],
			  [k4, v4],
			  [k5, v5]
			]);
		
		  var options = {title:'주제 : ${pVo.title}', 'width':700, 'height':400};
		  var chart = new google.visualization.PieChart(document.getElementById('chartView'));
		  chart.draw(data, options);
		}
		
		function changeChart() {
			var chartFlag = myform.choiceChart.value;
			location.href="${ctp}/vote/chartAnalysis?idx=${vos.get(0).popularIdx}&chartFlag="+chartFlag;
		}
		
	</script>
</head>
<body>
<div class="container">
  - 주제명 : ${pVo.title}<br/>
  - 투표 참여인원 : 총 ${voteTotCnt}명
  <form name="myform">
    <p>선택 :
    <%-- 
    <select name="choiceChart" onchange="changeChart()">
      <option value="0" <c:if test="${flag==0}"> selected </c:if>>항목별 투표결과</option>
      <option value="1">연령별 투표결과</option>
      <option value="2">지역별 투표결과</option>
      <option value="3">성별 투표결과</option>
    </select>
     --%>
    <select name="choiceChart" onchange="changeChart()">
      <option value="pie" <c:if test="${chartFlag=='pie'}"> selected </c:if>>원형차트보기</option>
      <option value="bar" <c:if test="${chartFlag=='bar'}"> selected </c:if>>막대차트보기</option>
    </select>
  </form>
  <div id="chartView"></div>
</div>
</body>
</html>