<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>dbOption.jsp(상품의 옵션 등록)</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"/>
  <script>
  	'use strict';
    let cnt = 1;
    
    // 옵션항목 추가
    function addOption() {
    	let strOption = "";
    	// 추가되는 옵션 id = t1, t2, t3 ...
    	let test = "t" + cnt; 
    	
    	strOption += '<div id="'+test+'"><hr size="5px"/>';
    	strOption += '<font size="4"><b>상품옵션등록</b></font>&nbsp;&nbsp;';
    	strOption += '<input type="button" value="옵션삭제" class="btn btn-outline-danger btn-sm" onclick="removeOption('+test+')"/><br/>'
    	strOption += '상품옵션이름';
    	strOption += '<input type="text" name="optionName" id="optionName'+cnt+'" class="form-control"/>';
    	strOption += '<div class="form-group">';
    	strOption += '상품옵션가격';
    	strOption += '<input type="text" name="optionPrice" id="optionPrice'+cnt+'" class="form-control"/>';
    	strOption += '</div>';
    	strOption += '</div>';
    	$("#optionType").append(strOption);
    	cnt++;
    }
    
    // 옵션항목 삭제
    function removeOption(test) {
    	// 아래와 같이 작성하면 삭제X
    	/* $("#"+test).remove(); */
    	// .id를 붙여야 삭제 됨
    	$("#"+test.id).remove();
    }
    
    // 옵션 입력후 등록전송
    function fCheck() {
    	for(let i=1; i<=cnt; i++) {
    		if($("#t"+i).length != 0 && document.getElementById("optionName"+i).value=="") {
    			alert("빈칸 없이 상품 옵션명을 모두 등록하셔야 합니다");
    			return false;
    		}
    		else if($("#t"+i).length != 0 && document.getElementById("optionPrice"+i).value=="") {
    			alert("빈칸 없이 상품 옵션가격을 모두 등록하셔야 합니다");
    			return false;
    		}
    	}
    	if(document.getElementById("optionName").value=="") {
    		alert("상품 옵션이름을 등록하세요");
    		return false;
    	}
    	else if(document.getElementById("optionPrice").value=="") {
    		alert("상품 옵션가격을 등록하세요");
    		return false;
    	}
    	else if(document.getElementById("productName").value=="") {
    		alert("상품명을 선택하세요");
    		return false;
    	}
    	else {
    		myform.submit();
    	}
    }
    
    // 상품 입력창에서 대분류 선택(Change)시 중분류가져와서 중분류 선택박스에 뿌리기
    function categoryMainChange() {
    	var categoryMainCode = myform.categoryMainCode.value;
			$.ajax({
				type : "post",
				url  : "${ctp}/dbShop/categoryMiddleName",
				data : {categoryMainCode : categoryMainCode},
				success:function(data) {
					var str = "";
					str += "<option value=''>중분류</option>";
					for(var i=0; i<data.length; i++) {
						str += "<option value='"+data[i].categoryMiddleCode+"'>"+data[i].categoryMiddleName+"</option>";
					}
					$("#categoryMiddleCode").html(str);
				},
				error : function() {
					alert("전송오류!");
				}
			});
  	}
    
    // 상품 입력창에서 중분류 선택(Change)시 소분류가져와서 소분류 선택박스에 뿌리기
    function categoryMiddleChange() {
    	var categoryMainCode = myform.categoryMainCode.value;
    	var categoryMiddleCode = myform.categoryMiddleCode.value;
			$.ajax({
				type : "post",
				url  : "${ctp}/dbShop/categorySubName",
				data : {
					categoryMainCode : categoryMainCode,
					categoryMiddleCode : categoryMiddleCode
				},
				success:function(data) {
					var str = "";
					str += "<option value=''>소분류</option>";
					for(var i=0; i<data.length; i++) {
						str += "<option value='"+data[i].categorySubCode+"'>"+data[i].categorySubName+"</option>";
					}
					$("#categorySubCode").html(str);
				},
				error : function() {
					alert("전송오류!");
				}
			});
  	}    
    
    // 상품 입력창에서 소분류 선택(Change)시 해당 상품들을 가져와서 품목 선택박스에 뿌리기
    function categorySubChange() {
    	var categoryMainCode = myform.categoryMainCode.value;
    	var categoryMiddleCode = myform.categoryMiddleCode.value;
    	var categorySubCode = myform.categorySubCode.value;
			$.ajax({
				type : "post",
				url  : "${ctp}/dbShop/categoryProductName",
				data : {
					categoryMainCode : categoryMainCode,
					categoryMiddleCode : categoryMiddleCode,
					categorySubCode : categorySubCode
				},
				success:function(data) {
					var str = "";
					str += "<option value=''>상품선택</option>";
					for(var i=0; i<data.length; i++) {
						str += "<option value='"+data[i].productName+"'>"+data[i].productName+"</option>";
					}
					$("#productName").html(str);
				},
				error : function() {
					alert("전송오류!");
				}
			});
  	}
    
    // 상품명을 선택하면 상품의 설명을 띄워준다.
    function productNameCheck() {
    	var productName = document.getElementById("productName").value;
    	$.ajax({
    		type:"post",
    		url : "${ctp}/dbShop/getProductInfor",
    		data: {productName : productName},
    		success:function(vo) {
    			let str = '<hr/><div class="row">';
    			str += '<div class="col">';
    			str += '대분류명 : '+vo.categoryMainName+'<br/>';
    			str += '중분류명 : '+vo.categoryMiddleName+'<br/>';
    			str += '소분류명 : '+vo.categorySubName+'<br/>';
    			str += '상 품 명 : '+vo.productName+'<br/>';
    			str += '상품제목 : '+vo.detail+'<br/>';
    			str += '상품가격 : '+numberWithCommas(vo.mainPrice)+'원<br/>';
    			str += '<input type="button" value="등록된옵션보기(삭제)" onclick="optionShow('+vo.idx+')" class="btn btn-info btn-sm"/><br/>';
    			str += '</div>';
    			str += '<div class="col">';
    			str += '<img src="${ctp}/dbShop/product/'+vo.fsname+'" width="160px"/><br/>';
    			str += '</div>';
    			str += '</div><hr/>';
    			str += '<div id="optionDemo"></div>';
    			$("#demo").html(str);
    			document.myform.productIdx.value = vo.idx;
    		},
    		error : function() {
    			alert("전송오류!");
    		}
    	});
    }
    
    // 옵션상세내역보기
    function optionShow(productIdx) {
    	$.ajax({
    		type : "post",
    		url  : "${ctp}/dbShop/getOptionList",
    		data : {productIdx : productIdx},
    		success:function(vos) {
    			let str = '';
    			if(vos.length != 0) {
						str = "옵션 : / ";
	    			for(let i=0; i<vos.length; i++) {
	    				str += '<a href="javascript:optionDelete('+vos[i].idx+')">';
							str += vos[i].optionName + "</a> / ";
	    			}
    			}
    			else {
    				str = "현 상품에 등록된 옵션이 없습니다.";
    			}
					$("#optionDemo").html(str);
    		},
    		error : function() {
    			alert("전송오류!");
    		}
    	});
    }
    
    // 옵션항목 삭제하기
    function optionDelete(idx) {
    	let ans = confirm("현재 선택한 옵션을 삭제하시겠습니까?");
    	if(!ans) return false;
    	
    	$.ajax({
    		type : "post",
    		url  : "${ctp}/dbShop/optionDelete",
    		data : {idx : idx},
    		success:function() {
    			alert("삭제되었습니다.");
    			location.reload();
    		},
    		error : function() {
    			alert("전송오류!");
    		}
    	});
    }
    
    // 콤마찍기
    function numberWithCommas(x) {
		  return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
  </script>
</head>
<body>
<p><br/></p>
<div class="container">
  <h2>상품에 따른 옵션 등록</h2>
  <form name="myform" method="post">
    <div class="form-group">
      <label for="categoryMainCode">대분류</label>
      <select id="categoryMainCode" name="categoryMainCode" class="form-control" onchange="categoryMainChange()">
        <option value="">대분류를 선택하세요</option>
        <c:forEach var="mainVo" items="${mainVos}">
        	<option value="${mainVo.categoryMainCode}">${mainVo.categoryMainName}</option>
        </c:forEach>
      </select>
    </div>
    <div class="form-group">
      <label for="categoryMiddleCode">중분류</label>
      <select id="categoryMiddleCode" name="categoryMiddleCode" class="form-control" onchange="categoryMiddleChange()">
        <option value="">중분류명</option>
      </select>
    </div>
    <div class="form-group">
      <label for="categorySubCode">소분류</label>
      <select id="categorySubCode" name="categorySubCode" class="form-control" onchange="categorySubChange()">
        <option value="">소분류명</option>
      </select>
    </div>
    <div class="form-group">
      <label for="productName">상품명(모델명)</label>
      <select name="productName" id="productName" class="form-control" onchange="productNameCheck()">
        <option value="">상품선택</option>
      </select>
      <div id="demo"></div>
    </div>
    <hr/>
    <font size="4"><b>상품옵션등록</b></font>&nbsp;&nbsp;
    <input type="button" value="옵션박스추가하기" onclick="addOption()" class="btn btn-secondary btn-sm"/><br/>
    <div class="form-group">
      <label for="optionName">상품옵션이름</label>
      <input type="text" name="optionName" id="optionName" class="form-control"/>
    </div>
    <div class="form-group">
      <label for="optionPrice">상품옵션가격</label>
      <input type="text" name="optionPrice" id="optionPrice" class="form-control"/>
    </div>
    <div id="optionType"></div>
    <hr/>
    <div class='text-right'><input type="button" value="옵션등록" onclick="fCheck()" class="btn btn-primary"/></div>
    <input type="hidden" name="productIdx">
  </form>
</div>
<p><br/></p>
</body>
</html>