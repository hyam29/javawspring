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
  <title>dbProductContent.jsp(상품정보 상세보기)</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"/>
  <script>
  	'use strict';
  	
  	// 배열의 개수 지정없이 동적배열로 설정하기 위해 전역변수 선언
  	let idxArray = new Array();
    
  	// 옵션박스에서, 옵션항목을 선택하였을때 처리하는 함수
    $(function(){
    	$("#selectOption").change(function(){
    		let selectOption = $(this).val();
    		let idx = selectOption.substring(0,selectOption.indexOf(":")); // 현재 옵션의 고유번호
    		let optionName = selectOption.substring(selectOption.indexOf(":")+1,selectOption.indexOf("_")); // 옵션명
    		let optionPrice = selectOption.substring(selectOption.indexOf("_")+1); // 옵션가격
    		let commaPrice = numberWithCommas(optionPrice); // 콤마붙인 가격
    		
    		// 선택박스의 내용을 한개라도 선택하게 된다면, 선택된 옵션의 '고유번호/옵션명/콤마붙인가격'을 화면에 출력 처리
    		if($("#layer"+idx).length == 0 && selectOption != "") { // 옵션이 하나라도 있으면 처리할 것이라는 코드
    		  idxArray[idx] = idx;
    		  
	    		let str = '';
	    		str += '<div class="layer row" id="layer'+idx+'"><div class="col">'+optionName+'</div>';
	    		str += '<input type="number" class="text-center numBox" id="numBox'+idx+'" name="optionNum" onchange="numChange('+idx+')" value="1" min="1"/> &nbsp;';
	    		str += '<input type="text" id="imsiPrice'+idx+'" class="price" value="'+commaPrice+'" readonly />';
	    		str += '<input type="hidden" id="price'+idx+'" value="'+optionPrice+'"/> &nbsp;';			/* 변동되는 가격을 재계산하기위해 price+idx 아이디를 사용하고 있다. */
	    		str += '<input type="button" class="btn btn-outline-danger btn-sm" onclick="remove('+idx+')" value="삭제"/>'; // 상품의 고유번호로 만들어진 배열방의 idx
	    		str += '<input type="hidden" name="statePrice" id="statePrice'+idx+'" value="'+optionPrice+'"/>';		/* 현재상태에서의 변경된 상품(옵션)의 가격이다. */
	    		str += '<input type="hidden" name="optionIdx" value="'+idx+'"/>';
	    		str += '<input type="hidden" name="optionName" value="'+optionName+'"/>';
	    		str += '<input type="hidden" name="optionPrice" value="'+optionPrice+'"/>';
	    		str += '</div>';
	    		$("#product1").append(str);
	    		onTotal(); // 계산함수 : 옵션선택 시 마다 가격이 달라져야하므로 동적폼의 마지막에 무조건 필요함
    	  }
    	  else {
    		  alert("이미 선택한 옵션입니다.");
    	  }
    	});
    });
    
    // 등록(추가)시킨 옵션 상품 삭제하기
    function remove(idx) {
  	  $("div").remove("#layer"+idx);
  	  onTotal();
    }
    
    // 상품의 총 금액 (재)계산하기
    function onTotal() {
  	  let total = 0;
  	  for(let i=0; i<idxArray.length; i++) {
  		  if($("#layer"+idxArray[i]).length != 0) {
		  	  //alert(idxArray[i]);
  		  	total +=  parseInt(document.getElementById("price"+idxArray[i]).value);
  		  	document.getElementById("totalPriceResult").value = total;
  		  }
  	  }
  	  document.getElementById("totalPrice").value = numberWithCommas(total);
    }
    
    // 수량 변경시 처리하는 함수
    function numChange(idx) {
    	let price = document.getElementById("statePrice"+idx).value * document.getElementById("numBox"+idx).value;
    	document.getElementById("imsiPrice"+idx).value = numberWithCommas(price);
    	document.getElementById("price"+idx).value = price;
    	onTotal();
    }
    
    // 장바구니 호출시 수행함수
    function cart() {
    	if(document.getElementById("totalPrice").value==0) {
    		alert("옵션을 선택해주세요");
    		return false;
    	}
    	else {
    		document.myform.submit();
    	}
    }
    
    // 직접 주문하기
    function order() {
    	let totalPrice = document.getElementById("totalPrice").value;
    	if('${sMid}' == "") {
    		alert("로그인 후 이용 가능합니다.");
    		location.href = "${ctp}/member/memLogin";
    	}
    	else if(totalPrice=="" || totalPrice==0) {
    		alert("옵션을 선택해주세요");
    		return false;
    	}
    	else {
    		document.getElementById("flag").value = "order";
    		document.myform.submit();
    	}
    }
    
    // 천단위마다 콤마를 표시해 주는 함수
    function numberWithCommas(x) {
    	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g,",");
    }
  </script>
  <style>
    .layer  {
      border:0px;
      width:100%;
      padding:10px;
      margin-left:1px;
      background-color:#eee;
    }
    .numBox {width:40px}
    .price  {
      width:160px;
      background-color:#eee;
      text-align:right;
      font-size:1.2em;
      border:0px;
      outline: none;
    }
    .totalPrice {
      text-align:right;
      margin-right:10px;
      color:#f63;
      font-size:1.5em;
      font-weight: bold;
      border:0px;
      outline: none;
    }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
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
		  <div class="text-left"><h3>${productVo.detail}</h4></div>
		  <div>
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
		          <option value="${vo.idx}:${vo.optionName}_${vo.optionPrice}">${vo.optionName}</option>
		        </c:forEach>
		      </select>
		    </form>
		  </div>
		  <br/>
		  <div>
			  <form name="myform" method="post">  <!-- 실제 상품의 정보를 넘겨주기 위한 form -->
			    <input type="hidden" name="mid" value="${sMid}"/>
			    <input type="hidden" name="productIdx" value="${productVo.idx}"/>
			    <input type="hidden" name="productName" value="${productVo.productName}"/>
			    <input type="hidden" name="mainPrice" value="${productVo.mainPrice}"/>
			    <input type="hidden" name="thumbImg" value="${productVo.FSName}"/>
			    <input type="hidden" name="totalPrice" id="totalPriceResult"/>
			    <input type="hidden" name="flag" id="flag"/>

			    <div id="product1"></div>
			  </form>
		  </div>
		  <!-- 상품의 총가격(옵션포함가격) 출력처리 -->
		  <div>
		    <hr/>
		    <div class="text-left"><font size="4" color="black">총상품금액</font></div>
		    <p class="text-right">
		    	<!-- 아래의 id와 class이름인 totalPrice는 출력용으로 보여주기위해서만 사용한것으로 값의 전송시와는 관계가 없다. -->
	        <b><input type="text" class="totalPrice text-right" id="totalPrice" value="<fmt:formatNumber value='0'/>" readonly /></b>
		    </p>
		  </div>
		  <br/>
		  <!-- 장바구니보기/주문하기/계속쇼핑하기 처리 -->
		  <div class="text-center">
		    <button class="btn btn-success" onclick="cart()">장바구니담기</button>&nbsp;
		    <button class="btn btn-info" onclick="order()">주문하기</button>&nbsp;
		    <button class="btn btn-primary" onclick="location.href='${ctp}/dbShop/dbProductList';">계속쇼핑하기</button>
		    <%-- <button class="btn btn-warning" onclick="location.href='${ctp}/dbShop/dbCartList';">장바구니보기</button>&nbsp; --%>
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
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>