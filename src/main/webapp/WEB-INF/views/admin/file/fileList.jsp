<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>fileList.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
	  // 전체선택
	  $(function(){
	  	$("#checkAll").click(function(){
	  		if($("#checkAll").prop("checked")) {
	    		$(".chk").prop("checked", true);
	  		}
	  		else {
	    		$(".chk").prop("checked", false);
	  		}
	  	});
	  });
	  
	  // 선택항목 반전
	  $(function(){
	  	$("#reverseAll").click(function(){
	  		$(".chk").prop("checked", function(){
	  			return !$(this).prop("checked");
	  		});
	  	});
	  });
	  
	  // 파일 삭제하기(ajax처리하기)
	  function selectDelCheck() {
		  let chked = document.getElementByName("chk");
		  alert(chked);
	  	if(chk != 'on') {
	  		alert('삭제할 게시물을 선택하세요.');
	  		return false;
	  	}
	  	else if($(".chk").prop("checked", true)) {
	  		
	  	
		  	let ans = confirm("선택된 모든 게시물을 삭제 하시겠습니까?");
		  	if(!ans) return false;
		  	let delItems = "";
		  	for(let i=0; i<photoForm.chk.length; i++) {
		  		if(photoForm.chk[i].checked == true) delItems += photoForm.chk[i].value + "/";
		  	}
		  	alert(delItems);
				
		  	$.ajax({
		  		type : "post",
		  		url  : "${ctp}/admin/file/photoViewDelete",
		  		data : {delItems : delItems},
		  		success:function(res) {
		  			if(res == "1") {
		  				alert("삭제완료");
		  			  location.reload();
		  			}
		  		},
		  		error  :function() {
		  			alert("전송오류!!");
		  		}
		  	});
		  }
	  }
	  // 선택 삭제처리
	  function delCheck() {
	  	let ans = confirm("선택된 파일을 삭제하시겠습니까?");
	  	if(!ans) return false;
	  }
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <h2>서버 파일 리스트</h2>
  <hr/>
  <p>서버의 파일 경로 : ${ctp}/data/ckeditor/~~~파일명</p>
  <form name="photoForm" method="post">
	  <table class="table table-hover text-center">
	    <tr>
	      <td colspan="4">
	        <input type="checkbox" id="checkAll" onclick="checkAllCheck()"/>전체선택/해제 &nbsp;
	        <input type="checkbox" id="reverseAll" onclick="reverseAllCheck()"/>선택반전 &nbsp;
	        <input type="button" value="선택항목삭제" onclick="selectDelCheck()" class="btn btn-outline-danger btn-sm"/>
	      </td>
	    </tr>
	    <tr class="table-dark text-dark">
	      <th>선택</th><th>번호</th><th>파일명</th><th>그림파일</th>
	    </tr>
		  <c:forEach var="file" items="${files}" varStatus="st">
		    <tr>
		      <td><c:if test="${file != 'board'}"><input type="checkbox" name="chk" class="chk" value=""/></c:if></td>
		      <td>${st.count}</td>
		      <td>${file}</td>
		      <td>
		      	<c:if test="${file == 'board'}">폴더명</c:if>
		      	<c:if test="${file != 'board'}"><img src="${ctp}/data/ckeditor/${file}" width="150px"/></c:if>
		      </td>
		    </tr>
		  </c:forEach>
	  </table>
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>