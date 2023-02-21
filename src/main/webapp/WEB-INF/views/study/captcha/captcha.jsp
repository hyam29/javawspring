<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!-- 
  - CAPTCHA는 기계는 인식할 수 없으나 사람은 쉽게 인식할 수 있는 텍스트, 이미지를 통해 사람과 기계를 구별하는 프로그램이다.
  - preventDefault 란?
    a 태그나 submit 태그는 누르게 되면 href 를 통해 이동하거나 , 창이 새로고침하여 실행되는데, 이때 preventDefault 를 통해 이러한 동작을 막아준다.
    주로 사용되는 경우는
      1. a 태그를 눌렀을때도 href 링크로 이동하지 않게 할 경우
      2. form 안에 submit 역할을 하는 버튼을 눌렀어도 새로 실행하지 않게 하고싶을 경우 (submit은 작동됨)
-->
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>captcha.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"/>
  <style>
    #captchaImage {
      width : 230px;
      height : 50px;
      border: 3px dotted #A3C552;
      text-align: center;
      padding: 5px;
    }
  </style>
  <script>
      // captcha이미지 새로(다시) 만들기
      $(document).ready(function(){
	      $("#refreshBtn").click(function(e){
	        //e.preventDefault();
	        $.ajax({
	          type : "get",   
	          url  : "${ctp}/study/captchaImage",
	          async: false
	        });
	      });
	      
	      // captcha 확인하기
	      $("#confirm‎Btn").click(function(e){
	        e.preventDefault();
	        
	        var strCaptcha = $("#strCaptcha").val();
	
	        $.ajax({   
	          type: "POST",   
	          url : "${ctp}/study/captcha",
	          data : {strCaptcha : strCaptcha}
	        }).done(function(result) { 
	          if (result=="1")    {
	              alert('로봇이 아니시군요. 계속 진행하세요.');
	          }else{
	              alert('로봇같은데요?... 다시 입력해보세요.');
	          }
	        });
	      });
      });

  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
<div class="container">
  <h2>Captcha Test!!!</h2>
  <form name="myform">
    <p>다음 코드를 입력해 주세요 : <img src="${ctp}/images/captcha.png" id="captchaImage"/></p>
    <p>
	    <input type="text" name="strCaptcha" id="strCaptcha"/>
	    <button id="confirm‎Btn">확인</button>
	    <button id="refreshBtn">새로고침</button>
    </p>
    <br/>
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>