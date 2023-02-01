<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>dbShopContent.jsp(상품정보 상세보기)</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"/>
  <script>
  	'use strict';
    
  	// 숫자 천단위마다 콤마(,)찍는 함수
    function numberWithCommas(x) {
    	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g,",");
    }
  </script>
</head>
<body>
<p><br/></p>
<div class="container">
  <div class="row">
    <div class="col p-3 text-center" style="border:1px solid #ccc">
		  <!-- 상품메인 이미지 -->
		  <div>
		    <img src="${ctp}/dbShop/product/${productVo.FSName}" width="100%"/>
		  </div>
		</div>
		<div class="col p-3 text-right">
		  <!-- 상품 기본정보 -->
		  <div id="iteminfor">
		    <h3>${productVo.detail}</h4>
		    <h3><font color="orange"><fmt:formatNumber value="${productVo.mainPrice}"/>원</font></h3>
		    <h4>${productVo.productName}</h3>
		  </div>
		  <!-- 상품주문을 위한 옵션정보 출력 -->
		  <div class="form-group">
		    <form name="optionForm">  <!-- 옵션의 정보를 보여주기위한 form -->
		      <select size="1" class="form-control" id="selectOption">
		        <option value="" disabled selected>상품옵션선택</option>
		        <option value="0:기본품목_${productVo.mainPrice}">기본품목</option>
		        <c:forEach var="vo" items="${optionVos}">
		        	<!-- :, _ 구분자 사용 -> ('/'만으로 구분자 사용하면 헷갈릴 수 있으므로) -->
		          <option value="${vo.idx}:${vo.optionName}_${vo.optionPrice}">${vo.optionName}</option>
		        </c:forEach>
		      </select>
		    </form>
		  </div>
		  <br/>
		  <div class="text-right p-2">
		    <input type="button" value="돌아가기" onclick="history.back()" class="btn btn-primary"/>
		  </div>
		</div>
  </div>
  <br/><br/>
  <!-- 상품 상세설명 보여주기 -->
  <div id="content" class="text-center"><br/>
    ${productVo.content}
  </div>
</div>
<p><br/></p>
</body>
</html>