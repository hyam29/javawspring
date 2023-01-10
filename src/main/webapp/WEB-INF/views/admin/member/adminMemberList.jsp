<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>adminMemberList.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
    'use strict';
    
    function midSearch() {
      let mid = myform.mid.value;
      if(mid.trim() == "") {
    	  alert("아이디를 입력하세요!");
    	  myform.mid.focus();
      }
      else {
    	  myform.submit();
      }
    }
    
    function delCheck(idx) {
    	let ans = confirm("해당회원을 탈퇴처리 하겠습니까?");
    	// if(ans) location.href='${ctp}/admin/member/adminMemberDel?pag=${pageVo.pag}&idx='+idx;
    	if(!ans) return false;
    	
    	$.ajax({
    		type : "post",
    		url : "${ctp}/admin/member/adminMemberDel",
    		data : {idx : idx},
    		success : function(res) {
    			if(res == "1") {
    				alert("해당회원의 탈퇴처리가 완료되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("탈퇴처리에 실패하였습니다.");
    			}
    		},
    		error : function() {
    			alert("탈퇴 전송 실패");
    		}
    	});
    	
    	
    }
    
    function searchCheck(e) {
    	let ans = confirm("해당 회원의 등급을 수정하시겠습니까?");
    	if(!ans) return false;
    	
    	let items = e.value.split("/");
    	
    	let query = {
    			idx : items[1],
    			level : items[0]
    	}
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/admin/member/adminMemberLevel",
    		data  : query,
    		success:function(res) {
    			if(res == "1") alert("해당 회원의 등급이 수정되었습니다.");
    			else alert("등급 수정에 실패하였습니다.");
	    		location.reload();
    		},
    		error : function() {
    			alert("전송 오류");
    		}
    	});
    }
  </script>
</head>
<body>
<p><br/></p>
<div class="container">
  <h2 class="text-center">* 전체 회원 리스트 *</h2>
  <br/>
  <form name="myform">
  	<div class="row mb-2">
  	  <div class="col form-inline">
  	    <input type="text" name="mid" class="form-control" autofocus />&nbsp;
  	    <input type="button" value="아이디개별검색" onclick="midSearch();" class="btn btn-secondary" />
  	  </div>
  	  <div class="col text-right"><button type="button" onclick="location.href='${ctp}/admin/member/adminMemberList';" class="btn btn-secondary">전체검색</button></div>
  	</div>
  </form>
  <table class="table table-hover text-center">
    <tr class="table-dark text-dark">
      <th>번호</th>
      <th>아이디</th>
      <th>별명</th>
      <th>성명</th>
      <th>최초가입일</th>
      <th>마지막접속일</th>
      <th>등급</th>
      <th>탈퇴유무</th>
    </tr>
    <c:set var="curScrStartNo" value="${pageVo.curScrStartNo}" />
    <c:forEach var="vo" items="${vos}" varStatus="st">
      <tr>
        <td>${curScrStartNo}</td>
        <td><a href="${ctp}/adMemInfor.ad?mid=${vo.mid}&pag=${pageVo.pag}">${vo.mid}</a></td>
        <td>${vo.nickName}</td>
        <td>${vo.name}<c:if test="${sLevel == 0 && vo.userInfor == '비공개'}"><font color='red'>(비공개)</font></c:if></td>
        <td>${fn:substring(vo.startDate,0,19)}</td>
        <td>${fn:substring(vo.lastDate,0,19)}</td>
        <td>
          <form name="levelForm">
            <!-- <select name="level" onchange="javascript:alert('회원정보를 변경하시려면, 등급변경버튼을 클릭하세요.');"> -->
            <select name="level" onchange="searchCheck(this)">
              <option value="0/${vo.idx}" <c:if test="${vo.level==0}">selected</c:if>>관리자</option>
              <option value="1/${vo.idx}" <c:if test="${vo.level==1}">selected</c:if>>운영자</option>
              <option value="2/${vo.idx}" <c:if test="${vo.level==2}">selected</c:if>>우수회원</option>
              <option value="3/${vo.idx}" <c:if test="${vo.level==3}">selected</c:if>>정회원</option>
              <option value="4/${vo.idx}" <c:if test="${vo.level==4}">selected</c:if>>준회원</option>
            </select>
            <%-- 
            <input type="submit" value="등급변경" class="btn btn-warning btn-sm"/>
            <input type="hidden" name="idx" value="${vo.idx}"/>
            --%>
          </form>
        </td>
        <td>
          <c:if test="${vo.userDel=='OK'}"><a href="javascript:delCheck(${vo.idx})"><font color="red">탈퇴신청</font></a></c:if>
          <c:if test="${vo.userDel!='OK'}">활동중</c:if>
        </td>
      </tr>
    <c:set var="curScrStartNo" value="${curScrStartNo-1}" />
    </c:forEach>
    <tr><td colspan="8" class="m-0 p-0"></td></tr>
  </table>
</div>
<br/>
<!-- 블록 페이지 시작 -->
<div class="text-center">
  <ul class="pagination justify-content-center">
    <c:if test="${pageVo.pag > 1}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=1">첫페이지</a></li>
    </c:if>
    <c:if test="${pageVo.curBlock > 0}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${(pageVo.curBlock-1)*pageVo.blockSize + 1}">이전블록</a></li>
    </c:if>
    <c:forEach var="i" begin="${(pageVo.curBlock)*pageVo.blockSize + 1}" end="${(pageVo.curBlock)*pageVo.blockSize + pageVo.blockSize}" varStatus="st">
      <c:if test="${i <= pageVo.totPage && i == pageVo.pag}">
    		<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/admin/member/adminMemberList?pag=${i}">${i}</a></li>
    	</c:if>
      <c:if test="${i <= pageVo.totPage && i != pageVo.pag}">
    		<li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${i}">${i}</a></li>
    	</c:if>
    </c:forEach>
    <c:if test="${pageVo.curBlock < pageVo.lastBlock}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${(pageVo.curBlock+1)*pageVo.blockSize + 1}">다음블록</a></li>
    </c:if>
    <c:if test="${pageVo.pag < pageVo.totPage}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${pageVo.totPage}">마지막페이지</a></li>
    </c:if>
  </ul>
</div>
<!-- 블록 페이지 끝 -->
<p><br/></p>
</body>
</html>