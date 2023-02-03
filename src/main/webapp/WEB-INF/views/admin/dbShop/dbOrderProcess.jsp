<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <meta charset="UTF-8">
  <title>dbOrderProcess.jsp(회원 주문확인)</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"/>
  <style>
    * {font-size: 1em}
  </style>
  <script>
    'use strict';
    
    $(document).ready(function() {
		  //document.getElementById("startJumun").value = ${startJumun};
		  //document.getElementById("endJumun").value = ${endJumun};
			document.getElementById("startJumun").value = new Date('${startJumun}').toISOString().substring(0,10);
			document.getElementById("endJumun").value = new Date('${endJumun}').toISOString().substring(0,10);
    });
  
    function nWin(orderIdx) {
    	var url = "${ctp}/dbShop/dbOrderBaesong?orderIdx="+orderIdx;
    	window.open(url,"dbOrderBaesong","width=350px,height=400px");
    }
    
    $(document).ready(function() {
    	// 주문/배송일자 조회
    	$("#orderStatus").change(function() {
	    	let startJumun = document.getElementById("startJumun").value;
	    	let endJumun = document.getElementById("endJumun").value;
	    	let orderStatus = $(this).val();
	    	location.href="${ctp}/dbShop/adminOrderStatus?startJumun="+startJumun+"&endJumun="+endJumun+"&orderStatus="+orderStatus;
    	});
    	
    	// 주문상태조회
    	$("#orderDateSearch").click(function() {
	    	let startJumun = document.getElementById("startJumun").value;
	    	let endJumun = document.getElementById("endJumun").value;
	    	let orderStatus = $(this).val();
	    	location.href="${ctp}/dbShop/adminOrderStatus?startJumun="+startJumun+"&endJumun="+endJumun+"&orderStatus="+orderStatus;
    	});
    });
    
    function orderProcess(orderIdx) {
    	let orderStatus = document.getElementById("goodsStatus"+orderIdx).value;
    	let ans = confirm("주문상태("+orderStatus+")를 변경하시겠습니까?");
    	if(!ans) {
    		location.reload();
    		return false;
    	}
  		let query = {
  				orderIdx : orderIdx,
  				orderStatus : orderStatus
  		};
  		$.ajax({
  			type  : "post",
  			url   : "${ctp}/dbShop/goodsStatus",
  			data  : query,
  			success : function(data) {
  				location.reload();
  			},
  			error : function() {
  				alert("전송오류!");
  			}
    	});
    }
  </script>
</head>
<body>
<c:set var="orderStatus" value="${orderStatus}"/>
<%-- <c:if test="${orderStatus eq ''}"><c:set var="orderStatus" value="전체"/></c:if> --%>
<p><br/></p>
<div class="container">
  <h2>주문/배송 확인</h2>
  <hr/>
  <table class="table table-borderless">
    <tr>
      <td>주문/배송일자 조회 :
        <input type="date" name="startJumun" id="startJumun"/> ~<input type="date" name="endJumun" id="endJumun"/>
        <button type="button" id="orderDateSearch" class="btn btn-outline-secondary m-0 p-1">조회</button>
      </td>
      <td align="right">(날짜선택후)주문상태조회 :
        <select name="orderStatus" id="orderStatus">
          <option value="전체"    ${orderStatus == '전체'    ? 'selected' : ''}>전체</option>
          <option value="결제완료" ${orderStatus == '결제완료' ? 'selected' : ''}>결제완료</option>
          <option value="배송중"  ${orderStatus == '배송중'   ? 'selected' : ''}>배송중</option>
          <option value="배송완료" ${orderStatus == '배송완료' ? 'selected' : ''}>배송완료</option>
          <option value="구매완료" ${orderStatus == '구매완료' ? 'selected' : ''}>구매완료</option>
          <option value="반품처리" ${orderStatus == '반품처리' ? 'selected' : ''}>반품처리</option>
        </select>
      </td>
    </tr>
  </table>
  <table class="table table-hover">
    <tr style="text-align:center;background-color:#ccc;">
      <th>순번</th>
      <th>주문정보</th>
      <th>상품</th>
      <th>주문내역</th>
      <th>주문일시</th>
    </tr>
    <c:set var="curScrStartNo" value="${pageVo.curScrStartNo}"/>
    <c:forEach var="vo" items="${vos}">
      <tr>
        <td class="text-center">${curScrStartNo}</td>
        <td style="text-align:center;">
          <p>주문번호 : ${vo.orderIdx}</p>
          <p>총 주문액 : <fmt:formatNumber value="${vo.totalPrice}"/>원</p>
          <p><input type="button" value="배송지정보" onclick="nWin('${vo.orderIdx}')"></p>
        </td>
        <td style="text-align:center;"><br/><img src="${ctp}/data/dbShop/product/${vo.thumbImg}" class="thumb" width="100px"/></td>
        <td align="left">
	        <p>모델명 : <span style="color:orange;font-weight:bold;">${vo.productName}</span><br/> &nbsp; &nbsp; <fmt:formatNumber value="${vo.mainPrice}"/>원</p>
	        <c:set var="optionNames" value="${fn:split(vo.optionName,',')}"/>
	        <c:set var="optionPrices" value="${fn:split(vo.optionPrice,',')}"/>
	        <c:set var="optionNums" value="${fn:split(vo.optionNum,',')}"/>
	        <p>
	          - 주문 내역
	          <c:if test="${fn:length(optionNames) > 1}">(옵션 ${fn:length(optionNames)-1}개 포함)</c:if><br/>
	          <c:forEach var="i" begin="1" end="${fn:length(optionNames)}">
	            &nbsp; &nbsp; ㆍ ${optionNames[i-1]} / <fmt:formatNumber value="${optionPrices[i-1]}"/>원 / ${optionNums[i-1]}개<br/>
	          </c:forEach>
	        </p>
	      </td>
        <td style="text-align:center;">
          주문자아이디:<font color="blue"> ${vo.mid}</font><br/><br/>
          주문일자 : ${fn:substring(vo.orderDate,0,10)}<br/><br/>
          <form name="myform">
	          <div>
	            주문상태:
		          <select name="goodsStatus" id="goodsStatus${vo.orderIdx}" onchange="orderProcess(${vo.orderIdx})">
			          <option value="결제완료" ${vo.orderStatus == '결제완료' ? 'selected' : ''}>결제완료</option>
			          <option value="배송중"  ${vo.orderStatus == '배송중' ? 'selected' : ''}>배송중</option>
			          <option value="배송완료"  ${vo.orderStatus == '배송완료' ? 'selected' : ''}>배송완료</option>
			          <option value="구매완료"  ${vo.orderStatus == '구매완료' ? 'selected' : ''}>구매완료</option>
			          <option value="반품처리"  ${vo.orderStatus == '반품처리' ? 'selected' : ''}>반품처리</option>
		          </select>
	          </div>
	        </form>
          <%-- <button type="button" onclick="orderProcess('${vo.orderIdx}','${vo.orderStatus}')" class="btn btn-outline-secondary btn-sm">${vo.orderStatus}</button> --%>
        </td>
      </tr>
      <c:set var="curScrStartNo" value="${curScrStartNo - 1}"/>
    </c:forEach>
    <tr><td colspan="5" class="m-0 p-0"></td></tr>
  </table>
  <br/>
  <!-- 블록 페이징처리 시작 -->
	<div class="container">
		<ul class="pagination justify-content-center">
			<c:if test="${pageVo.totPage == 0}"><p style="text-align:center"><b>자료가 없습니다.</b></p></c:if>
			<c:if test="${pageVo.totPage != 0}">
			  <c:if test="${pageVo.pag != 1}">
			    <li class="page-item"><a href="${ctp}/dbShop/adminOrderStatus?pag=1&startJumun=${startJumun}&endJumun=${endJumun}&orderStatus=${orderStatus}" title="첫페이지" class="page-link text-secondary">◁◁</a></li>
			  </c:if>
			  <c:if test="${pageVo.curBlock > 0}">
			    <li class="page-item"><a href="${ctp}/dbShop/adminOrderStatus?pag=${(pageVo.curBlock-1)*pageVo.blockSize + 1}&startJumun=${startJumun}&endJumun=${endJumun}&orderStatus=${orderStatus}" title="이전블록" class="page-link text-secondary">◀</a></li>
			  </c:if>
			  <c:forEach var="i" begin="${(pageVo.curBlock*pageVo.blockSize)+1}" end="${(pageVo.curBlock*blockSize)+pageVo.blockSize}">
			    <c:if test="${i == pageVo.pag && i <= pageVo.totPage}">
			      <li class="page-item active"><a href='${ctp}/dbShop/adminOrderStatus?pag=${i}&startJumun=${startJumun}&endJumun=${endJumun}&orderStatus=${orderStatus}' class="page-link text-light bg-secondary border-secondary">${i}</a></li>
			    </c:if>
			    <c:if test="${i != pageVo.pag && i <= pageVo.totPage}">
			      <li class="page-item"><a href='${ctp}/dbShop/adminOrderStatus?pag=${i}&startJumun=${startJumun}&endJumun=${endJumun}&orderStatus=${orderStatus}' class="page-link text-secondary">${i}</a></li>
			    </c:if>
			  </c:forEach>
			  <c:if test="${pageVo.curBlock < pageVo.lastBlock}">
			    <li class="page-item"><a href="${ctp}/dbShop/adminOrderStatus?pag=${(pageVo.curBlock+1)*pageVo.blockSize + 1}&startJumun=${startJumun}&endJumun=${endJumun}&orderStatus=${orderStatus}" title="다음블록" class="page-link text-secondary">▶</a>
			  </c:if>
			  <c:if test="${pageVo.pag != pageVo.totPage}">
			    <li class="page-item"><a href="${ctp}/dbShop/adminOrderStatus?pag=${pageVo.totPage}&startJumun=${startJumun}&endJumun=${endJumun}&orderStatus=${orderStatus}" title="마지막페이지" class="page-link" style="color:#555">▷▷</a>
			  </c:if>
			</c:if>
		</ul>
	</div>
	<!-- 블록 페이징처리 끝 -->
</div>
</body>
</html>