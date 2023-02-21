<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>thumbnail.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	function fCheck() {
		  let maxSize = 1024 * 1024 * 20;
		  let file = $("#file").val();
		  
		  if(file == "" || file == null) {
			  alert("업로드할 파일명을 선택해 주세요.");
			  return false;
		  }
		  
		  let ext = file.substring(file.lastIndexOf(".")+1);
		  let uExt = ext.toUpperCase();
		  if(uExt != "JPG" && uExt != "GIF" && uExt != "PNG") {
			  alert("업로드 가능한 파일은 '그림파일(JPG/GIF/PNG)' 입니다.");
			  return false;
	    }
		  if(file.Size > maxSize) {
			  alert("업로드할 파일의 최대용량은 20MByte 입니다.");
			  return false;
		  }
		  else {
			  myform.submit();
		  }
	  }
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <h2>썸네일 연습</h2>
  <form name="myform" method="post" enctype="multipart/form-data">
	  <p>파일명 :
	    <input type="file" name="file" id="file" class="form-control-file border" accept=".jpg,.gif,.png,.zip,.ppt,.pptx"/>
	  </p>
	  <p>
	    <input type="button" value="파일업로드" onclick="fCheck()" class="btn btn-success"/> &nbsp;
	    <input type="reset" value="다시선택" class="btn btn-warning"/> &nbsp;
	    <input type="button" onclick="location.href='${ctp}/study/thumbnailResult';" value="파일리스트로이동" class="btn btn-primary"/>
	  </p>
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>