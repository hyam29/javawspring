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
  	/* 전체 댓글(보기/숨김) */
  	$(document).ready(function() {
    	$("#reply").show();
    	$("#replyViewBtn").hide();
    	
    	$("#replyHiddenBtn").click(function() {
    		$("#reply").slideUp(500);
    		$("#replyViewBtn").show();
    		$("#replyHiddenBtn").hide();
    	});
    	
    	$("#replyViewBtn").click(function() {
    		$("#reply").slideDown(500);
    		$("#replyViewBtn").hide();
    		$("#replyHiddenBtn").show();
    	});
    });
  	
  	
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
		
 		// DB를 활용한 좋아요 토글처리.
    function goodDBCheck(idx) {
    	if(idx == "") idx = 0;
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/board/boardGoodDBCheck",
    		data  : {
    			idx  : idx,
				  part : 'board',
				  partIdx : ${vo.idx},
				  mid  : '${sMid}'
				},
    		success:function() {
    			location.reload();
    		},
    		error : function() {
    			alert("전송 오류~~");
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
				url : "${ctp}/board/boardReplyInput",
				data : query,
				success : function(res) {
					if(res == "1") {
						alert("댓글이 등록되었습니다.");
						location.reload();
					}
					else {
						alert("댓글 등록 실패");
					}
				},
				error : function() {
					alert("댓글 달기 전송 오류");
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
    		url   : "${ctp}/board/boardReplyDeleteOk",
    		data  : {idx : idx},
    		success:function() {
  					location.reload();
    		},
    		error  : function() {
    			alert("댓글 삭제 전송 오류");
    		}
    	});
		}
		
		/* 답변글(부모댓글의 댓글 = 대댓글) 처리 (동적폼) */
		function insertReply(idx, level, levelOrder, nickName) {
			let insReply = '';
			insReply += '<div class="container">';
			insReply += '<table class="m-2 p-0" style="width:90%">';
			insReply += '<tr>';
			insReply += '<td class="p-0 text-left">';
			insReply += '<div>';
			insReply += '답댓글 입력 : &nbsp;';
			insReply += '<input type="text" name="nickName" value="${sNickName}" size="6" readonly class="p-0 mt-2" />';
			insReply += '</div>';
			insReply += '</td>';
			insReply += '<td>';
			insReply += '<input type="button" value="답글" onclick="replyCheck2('+idx+','+level+','+levelOrder+')" />';
			insReply += '</td>';
			insReply += '</tr>';
			insReply += '<tr>';
			insReply += '<td colspan="2" class="text-center p-0">';
			insReply += '<textarea rows="3" name="content" id="content'+idx+'" class="form-control p-0">';
			insReply += '@'+nickName+'\n';
			insReply += '</textarea>';
			insReply += '</td>';
			insReply += '</tr>';
			insReply += '</table>';
			insReply += '</div>';
			
			$("#replyBoxOpenBtn"+idx).hide();
			$("#replyBoxCloseBtn"+idx).show();
			$("#replyBox"+idx).slideDown(500);
			$("#replyBox"+idx).html(insReply);
		}
		
		function closeReply(idx) {
			$("#replyBoxOpenBtn"+idx).show();
			$("#replyBoxCloseBtn"+idx).hide();
			$("#replyBox"+idx).slideUp(500);
		}
		
		/* 대댓글 작성 처리 (ajax) */
		function replyCheck2(idx, level, levelOrder) {
			let boardIdx = "${vo.idx}";
			let mid = "${sMid}";
			let nickName = "${sNickName}";
			// let content = "#content"+idx;
			// let contentVal = $(content).val();
			let content = $("#content"+idx).val();
			let hostIp = "${pageContext.request.remoteAddr}";
			
			if(content == "") {
				alert("내용(대댓글)을 입력해주세요.");
				$("#content"+idx).focus();
				return false;
			}
			
			let query = {
					boardIdx : boardIdx,
					mid : mid,
					nickName : nickName,
					content : content,
					hostIp : hostIp,
					level : level,
					levelOrder : levelOrder
			}
			
			$.ajax({
				type : "post",
				url : "${ctp}/board/boardReplyInput2",
				data : query,
				success : function() {
					location.reload();
				},
				error : function() {
					alert("대댓글 처리 전송 오류");
				}
			});
		}
		
		/* 댓글 수정 */
		function replyUpdate() {
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
				url : "${ctp}/board/boardReplyInput",
				data : query,
				success : function(res) {
					if(res == "1") {
						alert("댓글이 등록되었습니다.");
						location.reload();
					}
					else {
						alert("댓글 등록 실패");
					}
				},
				error : function() {
					alert("댓글 달기 전송 오류");
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
      		<a href="javascript:goodDBCheck(${goodVo.idx})">
            <c:if test="${!empty goodVo}"><font color="red">❤</font></c:if>
            <c:if test="${empty goodVo}">❤</c:if>
          </a> (DB처리 좋아요 토글)
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

<!-- 댓글(대댓글) 처리 시작 -->
<!-- 댓글 출력 창 시작 -->
<div class="text-center mb-3">
	<input type="button" value="댓글보기" id="replyViewBtn" class="btn btn-outline-info" /> 
	<input type="button" value="댓글숨김" id="replyHiddenBtn" class="btn btn-outline-dark" /> 
</div>
<div id="reply" class="container">
	<table class="table table-hover text-left">
		<tr class="table-warning text-dark">
			<th>작성자</th>
			<th>댓글 내용</th>
			<th class="text-center">작성일자</th>
			<th class="text-center">접속IP</th>
			<th class="text-center">댓글달기</th>
			<th>Edit</th>
		</tr>
		<c:forEach var="replyVo" items="${replyVos}">
			<tr>
				<td class="text-left">
	        <c:if test="${replyVo.level <= 0}">${replyVo.nickName}</c:if>	<!-- 부모댓글의 댓글인 경우, 들여쓰기XXX -->
	        <c:if test="${replyVo.level > 0}">
	        	<c:forEach var="i" begin="1" end="${replyVo.level}">&nbsp;&nbsp; </c:forEach>
	        	↪	${replyVo.nickName}
	        </c:if>
	      </td>
				<td>
					${fn:replace(replyVo.content, newLine, "<br/>")} 
				</td>
				<td class="text-center">${fn:substring(replyVo.WDate, 0, fn:length(replyVo.WDate)-2)}</td>
				<%-- <td class="text-center">${fn:substring(replyVo.WDate, 0, 19)}</td> --%>
				<td class="text-center">${replyVo.hostIp}</td>
				<td class="text-center">
					<!-- id="" : 답글 버튼은 대댓글 작성시 계속해서 필요 -> VO에 담긴 idx 붙여주면 고유한 버튼 됨! -->
					<!-- 댓글 고유번호, 댓글레벨,  -->
					<input type="button" value="댓글" onclick="insertReply('${replyVo.idx}','${replyVo.level}','${replyVo.levelOrder}','${replyVo.nickName}')" id="replyBoxOpenBtn${replyVo.idx}" class="btn btn-sm btn-outline-info" />
					<input type="button" value="닫기" onclick="closeReply(${replyVo.idx})" id="replyBoxCloseBtn${replyVo.idx}" class="btn btn-sm btn-dark" style="display:none;" />
				<td>
					<c:if test="${sLevel == 0 || sMid == replyVo.mid}">
						<input type="image" src="${ctp}/images/edit.png" title="댓글수정" width="25px" onclick="insertReply('${replyVo.idx}','${replyVo.level}','${replyVo.levelOrder}','${replyVo.nickName}')" id="replyBoxOpenBtn${replyVo.idx}" />
						<input type="image" src="${ctp}/images/delete.png" title="댓글삭제" width="25px" onclick="replyDelCheck(${replyVo.idx})" id="replyBoxOpenBtn${replyVo.idx}" />
						<%-- <a href="javascript:replyDelCheck(${replyVo.idx})" title="댓글삭제"><img src="${ctp}/images/delete.png" width="25px"></a>  --%>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="6" class="m-0 p-0" style="border-top:none;"><div id="replyBox${replyVo.idx}"></div></td>
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