<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>title</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
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
	  	$("#reversekAll").click(function(){
	  		$(".chk").prop("checked", function(){
	  			return !$(this).prop("checked");
	  		});
	  	});
	  });
	  
	  // 선택항목 삭제하기(ajax처리하기)
	  function selectDelCheck() {
	  	let ans = confirm("선택된 모든 게시물을 삭제 하시겠습니까?");
	  	if(!ans) return false;
	  	let delItems = "";
	  	/*
	  	for(let i=0; i<myform.chk.length; i++) {
	  		if(myform.chk[i].checked == true) delItems += myform.chk[i].value + "/";
	  	}
	  	*/
	  	
	  	// 체크박스의 개수 구하기     : $("input:checkbox[name=chk]").length;
	  	// 체크된 체크박수 개수 구하기 : $("input:checkbox[name=chk]:checked").length;
	  	
	  	// 배열처리명령어인 '.each'를 이용한 방법
	  	let i = 0;
	  	$("input:checkbox[name=chk]").each(function(){
	  		//if(this.checked) delItems += myform.chk[i].value + "/";
	  		if(this.checked) {
	  			delItems += $("#file"+i).val() + "/";
	  		}
	  		i++;
	  	});
	  	
	  	if(delItems == "") {
	  		alert("한개 이상을 선택후 처리하세요.");
	  		return false;
	  	}
	  	delItems = delItems.substring(0,delItems.length-1);	// 마지막 '/'제거하기
			
	  	$.ajax({
	  		type : "post",
	  		url  : "${ctp}/study/thumnailDelete",
	  		data : {delItems : delItems},
	  		success:function() {
  				alert("선택된 파일을 삭제처리 하였습니다.");
  			  location.reload();
	  		},
	  		error  :function() {
	  			alert("전송오류!!");
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
  <h2>저장된 썸네일 이미지 보기</h2>
  <hr/>
  <div class="row">
    <div class="col">서버의 파일 경로 : ${ctp}/data/thumbnail/~~~파일명</div>
    <div class="col text-right"><a href="${ctp}/study/thumbnail" class="btn btn-success">썸네일생성으로이동</a></div>
  </div>
  <hr/>
  <form name="myform">
	  <table class="table table-hover text-center">
	    <tr>
	      <td colspan="4" class="text-left">
	        <input type="checkbox" id="checkAll" onclick="checkAllCheck()"/>전체선택/해제 &nbsp;&nbsp;
	        <input type="checkbox" id="reversekAll" onclick="reverseAllCheck()"/>선택반전 &nbsp;&nbsp;
	        <input type="button" value="선택항목삭제" onclick="selectDelCheck()" class="btn btn-danger btn-sm"/>
	      </td>
	    </tr>
	    <tr class="table-dark text-dark">
	      <th>선택</th><th>번호</th><th>파일명</th><th>썸네일이미지</th>
	    </tr>
		  <c:forEach var="file" items="${files}" varStatus="st">
		    <tr>
		      <td><input type="checkbox" name="chk" class="chk" value="${file}"/></td>
		      <td>${st.count}</td>
		      <td>${file}</td>
		      <td>
		        <c:if test="${fn:substring(file,0,2) == 's_'}"><img src="${ctp}/data/thumbnail/${file}"/></c:if>
		        <c:if test="${fn:substring(file,0,2) != 's_'}">
		          <a href="${ctp}/data/thumbnail/${file}" target="_blank"><img src="${ctp}/data/thumbnail/${file}" width="150px"/></a>
		        </c:if>
		      </td>
		    </tr>
		    <input type="hidden" id="file${st.index}" value="${file}"/>
		  </c:forEach>
		  <tr><td colspan="4" class="m-0 p-0"></td></tr>
	  </table>
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>