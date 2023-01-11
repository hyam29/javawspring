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
  <title>boardContent.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
  	'use strict';
  	/* 좋아요 빨간색 하트 처리 -> db로 처리하게끔 만들어 보기(지금은 세션처리 되어있음) */
  	function goodCheck() {
  		$.ajax({
  			type: "post",
  			url: "${ctp}/board/boardGood",
  			data: {idx : ${vo.idx}},
  			success: function() {
  				location.reload();
  			},
  			error: function() {
  				alert("좋아요 전송 오류");
  			}
  		});
  	}
  	 /* data : idx는 int라서 그냥 작성! String이라면, idx: "$ { vo . 변수명}" 따옴표 꼭 필요!!!!!!! */
		
		// 좋아요 Plus버튼누르면 1증가처리 (Minus버튼클릭시 1감소처리)
    function goodCheckPlusMinus(flag) {
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/board/boardGoodPlusMinus",
    		data  : {
    			idx : ${vo.idx},
    			goodCnt : flag
    		},
    		success:function(res) {
    			if(res == "1") alert("이미 누르셨습니다.");
    			else location.reload();
    		}
    	});
    }
		
		// 게시글 삭제 처리
		function boardDelCheck() {
			let ans = confirm("게시글을 삭제하시겠습니까?");
			if(ans) location.href="${ctp}/board/boardDeleteOk?idx=${vo.idx}&pageSize=${pageSize}&pag=${pag}";
		}
		// onclick 함수에서 매개변수 idx를 넘겨줬다면, 컨트롤러에 값 넘길 때 + idx 만 적으면 됨 
		
		
		// 댓글 달기 처리
		function replyCheck() {
			let content = $("#content").val();
			if(content.trim() == "") {
				alert("댓글 내용을 입력하세요.");
				$("#content").focus();
				return false;
			}
			let query = {
					boardIdx : ${vo.idx},
					mid : '${sMid}',
					nickName : '${sNickName}',
					content : content,
					hostIp : '${pageContext.request.remoteAddr}'
			}
			
			$.ajax({
				type : "post",
				url : "${ctp}/boReplyInput.bo",
				data : query,
				success : function(res) {
					if(res == "1") {
						alert("댓글이 등록되었습니다.");
						location.reload();
					}
					else {
						alert("댓글 등록 실패.");
					}
				},
				error : function() {
					alert("댓글 달기 전송 오류.");
				}
				
			});
		}
		
		// 댓글 삭제하기
		// data 에 적은 값(뒤에 적은 idx)이 replyDelCheck(idx:원댓글의고유번호) 의 idx
    function replyDelCheck(idx) {
    	let ans = confirm("댓글을 삭제하시겠습니까?");
    	if(!ans) return false;
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/boReplyDeleteOk.bo",
    		data  : {idx : idx},
    		success:function(res) {
    			if(res == "1") {
    				alert("댓글이 삭제되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("댓글이 삭제처리 실패");
    			}
    		},
    		error  : function() {
    			alert("댓글 삭제 전송 오류");
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
	<h2 class="text-center">글 내 용 보 기</h2>
	<hr/>
	<br/>
	<table class="table table-borderless">
		<tr>
			<td class="text-right">hostIp : ${vo.hostIp}</td>
		</tr>
	</table>
	<table class="table table-bordered">
		<tr>
			<th>글쓴이</th>
			<td>${vo.nickName}</td>
			<th>작성일</th>
			<%-- <td>${fn:substring(vo.wDate,0,19)}</td> --%>
			<td>${fn:substring(vo.WDate,0,fn:length(vo.WDate)-2)}</td>
		</tr>
		<tr>
			<th>글제목</th>
			<td colspan="3">${vo.title}</td>
		</tr>
		<tr>
			<th>이메일</th>
			<td>${vo.email}</td>
			<th>조회수</th>
			<td>${vo.readNum}</td>
		</tr>
		<tr>
			<th>홈페이지(블로그)</th>
			<td>${vo.homePage}</td>
			<th>좋아요</th>
      <td><a href="javascript:goodCheck()">
            <c:if test="${sSw == '1'}"><font color="red">❤</font></c:if>
            <c:if test="${sSw != '1'}">❤</c:if>
          </a>
          ${vo.good} ,
          <a href="javascript:goodCheckPlusMinus(1)">👍</a>
          <a href="javascript:goodCheckPlusMinus(-1)">👎</a> , 
          
          <!-- idx, mid 넘겨줘야 함 -->
      		<a href="javascript:goodDBCheck(${goodVo.goodSw})">
            <c:if test="${goodVo.goodSw == 'Y'}"><font color="red">❤</font></c:if>
            <c:if test="${goodVo.goodSw != 'Y'}">❤</c:if>
          </a> (DB처리 좋아요)
      </td>
    </tr>
		<tr>
			<th>글내용</th>
			<td colspan="3" style="height:220px">${fn:replace(vo.content, newLine, "<br/>")}</td>
		</tr>
		<tr>
			<td colspan="4" class="text-center">
				<c:if test="${flag == 'search'}"><input type="button" value="검색 목록으로 돌아가기" onclick="location.href='${ctp}/board/boardSearch?search=${search}&searchString=${searchString}&pageSize=${pageSize}&pag=${pag}';" class="btn btn-outline-primary" /></c:if>
				<c:if test="${flag != 'search'}">
					<input type="button" value="글 목록보기" onclick="location.href='${ctp}/board/boardList?pageSize=${pageSize}&pag=${pag}';" class="btn btn-outline-primary" />
					<c:if test="${sMid == vo.mid || sLevel == 0}">
						<input type="button" value="수정" onclick="location.href='${ctp}/board/boardUpdate?idx=${vo.idx}&pageSize=${pageSize}&pag=${pag}';" class="btn btn-outline-warning" />
						<!-- 콤보박스 조회 건수와 수정 후 페이지 유지를 하기 위해서는 pageSize와 pag 같이 넘겨주는 게 좋음 -->
						<input type="button" value="삭제" onclick="boardDelCheck()" class="btn btn-outline-danger" />
						<!-- 현재 보고 있는 글이 한개니까, idx 안넘겨도 됨, 삭제는 바로 삭제하는 것 보다 메세지 띄우는 게 좋으므로 함수 생성 -->
					</c:if>
				</c:if>
			</td>
		</tr>
	</table>
	
	<c:if test="${flag != 'search'}">
	<!-- 이전글/다음글 처리 -->
	  <table class="table table-borderless">
	    <tr>
	    	<td>
	      	<!--
	      	<td style="float:left">
		        <c:if test="${preVo.preIdx != 0}">
		          👈 <a href="${ctp}/board/boardContent?idx=${preVo.preIdx}&pageSize=${pageSize}&pag=${pag}">이전글 : ${preVo.preTitle}</a><br/>
		        </c:if>
		      </td>
		      <td style="float:right">
		        <c:if test="${nextVo.nextIdx != 0}">
		          👉 <a href="${ctp}/board/boardContent?idx=${nextVo.nextIdx}&pageSize=${pageSize}&pag=${pag}">다음글 : ${nextVo.nextTitle}</a>
		        </c:if>
		      </td>
          -->
          <c:if test="${!empty pnVos[1]}">
          	다음글 : <a href="${ctp}/board/boardContent?idx=${pnVos[1].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[1].title}</a><br/>
          </c:if>
          <c:if test="${vo.idx < pnVos[0].idx}">
          	다음글 : <a href="${ctp}/board/boardContent?idx=${pnVos[0].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[0].title}</a><br/>
        	</c:if>
          <c:if test="${vo.idx > pnVos[0].idx}">
          	이전글 : <a href="${ctp}/board/boardContent?idx=${pnVos[0].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[0].title}</a><br/>
        	</c:if>
	      </td>
	      
	    </tr>
	  </table>
  <!-- 이전글/다음글 처리 끝 -->
  </c:if>
</div>
<br/>
<!-- 댓글 출력 창 시작 -->
<div class="container">
	<table class="table table-hover text-left">
		<tr class="table-warning text-dark">
			<th>작성자</th>
			<th>댓글 내용</th>
			<th class="text-center">작성일자</th>
			<th class="text-center">접속IP</th>
			<th></th>
		</tr>
		<c:forEach var="replyVo" items="${replyVos}">
			<tr>
				<td>${replyVo.nickName}</td>	
				<td>${fn:replace(replyVo.content, newLine, "<br/>")}</td>
				<td class="text-center">${fn:substring(replyVo.WDate, 0, fn:length(replyVo.WDate)-2)}</td>
				<%-- <td class="text-center">${fn:substring(replyVo.WDate, 0, 19)}</td> --%>
				<td class="text-center">${replyVo.hostIp}</td>
				<td>
					<c:if test="${sLevel == 0 || sMid == replyVo.mid}">
						<a href="javascript:replyDelCheck(${replyVo.idx})" title="댓글삭제">✖</a> 
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
<!-- 댓글 출력 창 끝 -->

<!-- 댓글 입력창 시작 -->
	<form name="replyForm">
		<table class="table text-center">
			<tr>
				<td style="width:85%" class="text-left">
					<b>댓글내용</b>
					<textarea rows="4" name="content" id="content" class="form-control"></textarea>
				</td>
				<td style="width:15%">
					<br/>
					<p> 작성자 : ${sNickName}</p>
					<p>
						<input type="button" value="댓글등록" onclick="replyCheck()" class="btn btn-outline-info btn-sm" />
					</p>
				</td>
			</tr>
		</table>
	</form>
<!-- 댓글 입력창 끝 -->
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>