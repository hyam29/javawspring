<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>scheduleMenu.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
  	'use strict';
  	// 등록창닫기 버튼을 숨기기 위해 일단 전역변수 개념으로 jQuery로 작성 (form의 hidden도 불러와두기 위해서)
  	$(document).ready(function(){
  		$("#scheduleInputHidden").hide();
  	});
  	
  	// 일정등록 form을 가상창에 출력
  	function scheduleInputView() {
  		let scheduleInput = '<hr/><div id="scheduleInputForm">';
  		scheduleInput += '<form name="myform" method="post">';
  		scheduleInput += '<table class="table table-borderless>"';
  		scheduleInput += '<tr><th>일정분류</th><td>';
  		scheduleInput += '<select name="part" class="form-control">';
  		scheduleInput += '<option value="모임">모임</option>';
  		scheduleInput += '<option value="업무">업무</option>';
  		scheduleInput += '<option value="학습">학습</option>';
  		scheduleInput += '<option value="여행">여행</option>';
  		scheduleInput += '<option value="기타">기타</option>';
  		scheduleInput += '</select>';
  		scheduleInput += '</td></tr>';
  		scheduleInput += '<tr><th>내용</th><td>';
  		scheduleInput += '<textarea name="content" rows="4" class="form-control"></textarea>';
  		scheduleInput += '</td></tr>';
  		scheduleInput += '<tr><td colspan="2" class="text-center">';
  		scheduleInput += '<input type="button" value="일정등록" onclick="scheduleInputOk()" class="btn btn-outline-primary form-control" />';
  		scheduleInput += '</td></tr>';
  		scheduleInput += '</table>';
  		scheduleInput += '</form></div>';
  		
  		$("#scheduleInputView").hide();
    	$("#scheduleInputHidden").show();
    	$("#demo").html(scheduleInput);
  	}
  	
  	// 일정등록 창 닫기 처리
  	function scheduleInputHidden() {
  		$("#scheduleInputHidden").hide();
  		$("#scheduleInputForm").slideUp(500);
  		$("#scheduleInputView").show();
  	}
  	
  	// 일정 등록 처리 (ajax)
  	function scheduleInputOk() {
  		let part = myform.part.value;
  		let content = myform.content.value;
  		if(content == "") {
  			alert("일정을 입력하세요.");
  			myform.content.focus();
  			return false;
  		}
  		let query = {
  			mid : '${sMid}',
  			ymd : '${ymd}',
  			part : part,
  			content : content
  		}
  		
  		$.ajax({
  			type : "post",
  			url : "${ctp}/schedule/scheduleInputOk",
  			data : query,
  			success : function(res) {
					alert("일정등록이 완료되었습니다.");
					location.reload();
  			},
  			error : function() {
  				alert("전송 오류.");
  			}
  		});
  	}
 		// 스케줄 상세내역을 모달로 출력하기
		function modalView(part, content) {
			$("#myModal").on("show.bs.modal", function(e){
				$(".modal-body #part").html(part);
				$(".modal-body #content").html(content);
			});
		}
 		
 		// 일정 삭제 처리
 		function delCheck(idx) {
 			let ans = confirm("해당 일정을 삭제하시겠습니까?");
 			if(!ans) return false;
 			
 			$.ajax({
 				type : "post",
 				url : "${ctp}/schedule/scheduleDelete",
 				data : {idx : idx},
 				success : function(res) {
 					alert("해당 일정이 삭제되었습니다.");
 					location.reload();
 				},
 				error : function() {
 					alert("일정 삭제 전송 오류");
 				}
 			});
 		}
 		
 		// 일정수정 form을 가상창에 출력
  	function updateCheck(idx, part, content) {
    	let scheduleInput = '<div id="scheduleUpdateForm'+idx+'">';
    	scheduleInput += '<form name="updateForm'+idx+'">';
    	scheduleInput += '<table class="table table-bordered">';
    	scheduleInput += '<tr><th>일정분류</th><td>';
    	scheduleInput += '<select name="part" id="part'+idx+'" class="form-control">';
    	scheduleInput += '<option value="모임">모임</option>';
    	scheduleInput += '<option value="업무">업무</option>';
    	scheduleInput += '<option value="학습">학습</option>';
    	scheduleInput += '<option value="여행">여행</option>';
    	scheduleInput += '<option value="기타">기타</option>';
    	scheduleInput += '<option value="'+part+'" selected>'+part+'</option>';
    	scheduleInput += '</select>';
    	scheduleInput += '</td></tr>';
    	scheduleInput += '<tr><th>내용</th><td>';
    	scheduleInput += '<textarea name="content" id="content'+idx+'" rows="4" class="form-control">'+content.replaceAll("<br/>","\n")+'</textarea>';
    	scheduleInput += '</td></tr>';
    	scheduleInput += '<tr><td colspan="2" class="text-center">';
    	scheduleInput += '<span class="row">';
    	scheduleInput += '<span class="col"><input type="button" value="일정수정" onclick="scheduleUpdateOk('+idx+')" class="btn btn-outline-success form-control"/></span>';
    	scheduleInput += '<span class="col"><input type="button" value="수정창닫기" onclick="scheduleUpdateClose('+idx+')" class="btn btn-outline-warning form-control"/></span>';
    	scheduleInput += '</span>';
    	scheduleInput += '</td></tr>';
    	scheduleInput += '</table>';
    	scheduleInput += '</form></div>';
    	
    	$("#scheduleUpdateOpen"+idx).hide();
    	$("#updateDemo"+idx).slideDown(500);
    	$("#updateDemo"+idx).html(scheduleInput);
    }
  	
  	// 일정 수정창 닫기
    function scheduleUpdateClose(idx) {
    	$("#scheduleUpdateOpen"+idx).show();
    	$("#scheduleUpdateForm"+idx).slideUp(500);
    }
  	
  	/* 일정 수정 처리 */
  	function scheduleUpdateOk(idx) {
			let part = $("#part"+idx).val();
    	let content = $("#content"+idx).val();
			if(content == '') {
				alert("수정할 내용을 입력해주세요.") 
				return false;
			}
    	
  		$.ajax({
  			type : "post",
  			url : "${ctp}/schedule/scheduleUpdateOk",
  			data : {idx : idx, part : part, content : content},
  			success : function(res) {
  				alert("일정이 수정되었습니다.");
  				location.reload();
  			},
  			error : function() {
  				alert("일정수정 오류");
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
	<h4><font color="lightsalmon">${ymd} 의 일정입니다.</font></h4>
	<p>오늘의 일정은 총 <b>${scheduleCnt}건</b> 입니다.</p>
  <hr/>
  <div>
    <input type="button" value="일정등록" onclick="scheduleInputView()" id="scheduleInputView" class="btn btn-outline-primary"/>
    <input type="button" value="등록창닫기" onclick="scheduleInputHidden()" id="scheduleInputHidden" class="btn btn-outline-secondary"/>
    <input type="button" value="돌아가기" onclick="location.href='${ctp}/schedule/schedule?yy=${fn:split(ymd,'-')[0]}&mm=${fn:split(ymd,'-')[1]-1}';" class="btn btn-outline-dark"/>
  </div>
  <div id="demo"></div>
  <hr/>
  <c:if test="${scheduleCnt != 0}">
    <table class="table table-hover text-center">
      <tr class="table-dark text-dark">
        <th>번호</th>
        <th>간단 내역</th>
        <th>분류</th>
        <th>비고</th>
      </tr>
      <c:forEach var="vo" items="${vos}" varStatus="st">
        <tr>
          <td>${st.count}</td>
          <td>
          	<a href="#" onclick="modalView('${vo.part}','${fn:replace(vo.content,newLine,'<br/>')}')" data-toggle="modal" data-target="#myModal">
	            <c:if test="${fn:indexOf(vo.content,newLine) != -1}">${fn:substring(vo.content,0,fn:indexOf(vo.content,newLine))}</c:if>
	            <c:if test="${fn:indexOf(vo.content,newLine) == -1}">${fn:substring(vo.content,0,20)}</c:if>
            </a>
          </td>
          <td>${vo.part}</td>
          <td>
            <input type="button" value="수정" onclick="updateCheck('${vo.idx}','${vo.part}','${fn:replace(vo.content,newLine,'<br/>')}')" id="scheduleUpdateOpen${vo.idx}" class="btn btn-outline-warning btn-sm"/>
            <input type="button" value="삭제" onclick="delCheck(${vo.idx})" class="btn btn-outline-danger btn-sm"/>
          </td>
        </tr>
        <tr><td colspan="4" class="m-0 p-0"><div id="updateDemo${vo.idx}"></div></td></tr>
      </c:forEach>
      <tr><td colspan="4" class="m-0 p-0"></td></tr>
    </table>
  </c:if>
</div>

<!-- The Modal(모달창 클릭시 자료실의 내용을 모달창에 출력한다. -->
<div class="modal fade" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
    
      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title"><b>${ymd}</b></h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>
      
      <!-- Modal body -->
      <div class="modal-body">
        <table class="table">
          <tr><th>분류 :</th><td><span id="part"></span></td></tr>
          <tr><th>내용 :</th><td><span id="content"></span></td></tr>
          <tr><th>작성자 :</th><td>${sMid}</td></tr>
        </table>
      </div>
      
      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>
      
    </div>
  </div>
</div>

<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>