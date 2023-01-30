<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>wmList.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@900&display=swap" rel="stylesheet">
	<style>
		body {font-family: 'Gowun Batang', serif;}
	</style>
	<script>
		'use strict';
		setTimeout("location.reload()",1000*10);
		
		// 보낸메세지함에서의 삭제 처리 (휴지통XXX)
		function msgDel(idx) {
			let ans = confirm("해당 메세지를 삭제하시겠습니까?");
			if(!ans) return false;
			
			let query = {
					idx : idx,
					mFlag : '${mSw}'
			}
			
			$.ajax({
				type : "post",
				url : "${ctp}/webMessage/wmDelete",
				data : query,
				success : function(res) {
					alert("해당 메세지가 삭제되었습니다.");
					location.reload();
				}
			});
		}
	</script>
</head>
<body>
<p><br/></p>
<!-- 
	mSw 	=> 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
	mFlag => 0:메세지작성, 1:받은메세지, 2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지내용보기
 -->
<div class="container">
	<table class="table text-hover">
		<tr class="table-dark text-dark">
			<th>번호</th>
			<th>
				<c:if test="${mSw == 1 || mSw == 2 || mSw == 5}">보낸이</c:if>
				<c:if test="${mSw == 3 || mSw == 4}">받는이</c:if>
			</th>
			<th>제목</th>
			<th>
				<c:if test="${mSw == 1 || mSw == 2 || mSw == 5}">보낸(확인)일자</c:if>
				<c:if test="${mSw == 3 || mSw == 4}">받은일자</c:if>
			</th>
		</tr>
		<c:set var="curScrStartNo" value="${pageVo.curScrStartNo}" />
		<c:forEach var="vo" items="${vos}">
			<tr>
				<td>${curScrStartNo}</td>
				<td>
					<c:if test="${mSw == 1 || mSw == 2 || mSw == 5}">${vo.sendId}</c:if>
					<c:if test="${mSw == 3 || mSw == 4}">${vo.receiveId}</c:if>
				</td>
				<!-- $ { param .변수명} : url에 붙은 값을 붙들고 갈 때 사용 가능 (aJax 사용 시 유용하게 사용) -> 컨트롤러에서 model에 안담았기 때문에 사용 -->
				<td>
					<a href="webMessage?mSw=6&mFlag=${param.mSw}&idx=${vo.idx}&receiveSw=${vo.receiveSw}">
						${vo.title}
					</a>
					<c:if test="${vo.receiveSw == 'n'}"><img src="${ctp}/images/new.gif" /></c:if>
					<c:if test="${mSw == 3 || mSw == 5}">
						<a href="javascript:msgDel(${vo.idx})"><img src="${ctp}/images/delete.png" style="width:20px" /></a>
					</c:if>
				</td>
				<td>
					<span style="font-size:0.8em">${fn:substring(vo.receiveDate,0,19)}</span>
				</td>
			</tr>
			<c:set var="curScrStartNo" value="${curScrStartNo-1}" />
		</c:forEach>
		<tr><td colspan="4" class="m-0 p-0"></td></tr>
	</table>
</div>

<!-- 블록 페이지 시작 -->
<div class="text-center">
  <ul class="pagination justify-content-center">
    <c:if test="${pageVo.pag > 1}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/webMessage/webMessage?mSw=${mSw}&mFlag=${param.mFlag}&pag=1">첫페이지</a></li>
    </c:if>
    <c:if test="${pageVo.curBlock > 0}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/webMessage/webMessage?mSw=${mSw}&mFlag=${param.mFlag}&pag=${(pageVo.curBlock-1)*pageVo.blockSize + 1}">이전블록</a></li>
    </c:if>
    <c:forEach var="i" begin="${(pageVo.curBlock)*pageVo.blockSize + 1}" end="${(pageVo.curBlock)*pageVo.blockSize + pageVo.blockSize}" varStatus="st">
      <c:if test="${i <= pageVo.totPage && i == pageVo.pag}">
    		<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/webMessage/webMessage?mSw=${mSw}&mFlag=${param.mFlag}&pag=${i}">${i}</a></li>
    	</c:if>
      <c:if test="${i <= pageVo.totPage && i != pageVo.pag}">
    		<li class="page-item"><a class="page-link text-secondary" href="${ctp}/webMessage/webMessage?mSw=${mSw}&mFlag=${param.mFlag}&pag=${i}">${i}</a></li>
    	</c:if>
    </c:forEach>
    <c:if test="${pageVo.curBlock < pageVo.lastBlock}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/webMessage/webMessage?mSw=${mSw}&mFlag=${param.mFlag}&pag=${(pageVo.curBlock+1)*pageVo.blockSize + 1}">다음블록</a></li>
    </c:if>
    <c:if test="${pageVo.pag < pageVo.totPage}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/webMessage/webMessage?mSw=${mSw}&mFlag=${param.mFlag}&pag=${pageVo.totPage}">마지막페이지</a></li>
    </c:if>
  </ul>
</div>
<!-- 블록 페이지 끝 -->

<p></p>
</body>
</html>