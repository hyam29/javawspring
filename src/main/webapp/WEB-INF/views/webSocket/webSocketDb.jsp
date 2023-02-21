<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>webSocket.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
  <style>
    /* 
    .container {
			width: 500px;
		}
		*/
		#list {
			height: 400px;
			padding: 15px;
			overflow: auto;
		}
  </style>
  <script>
	  $(document).ready(function(){
		  
			//채팅 서버 주소
		  let url = "ws://localhost:9090/javawspring/chatserverDb";
		     		
		  // 웹 소켓
		  let ws;
		
		  // 연결하기
		  $('#btnConnect').click(function() {
		  	// 유저명 확인
	     	if ($('#user').val().trim() != '') {
	     		// 연결
	  	   	ws = new WebSocket(url);
	  	   			
	  	   	// 소켓 이벤트 매핑(연결되면 'onopen'메소드가 수행된다.)
	  	   	ws.onopen = function (evt) {
	  	   		console.log('서버 연결 성공');
	  	   		print($('#user').val(), '입장했습니다.');
	  	   				
	  	   		// 현재 사용자가 입장했다고 서버에게 통지(유저명 전달)
	  	   		// -> 1#유저명
		  			ws.send('1#' + $('#user').val() + '#');
		  			
		  			$('#chatStatus').html('${sNickName}님 접속중');
	  	   		
		  			$('#user').attr('readonly', true);
		  			$('#btnConnect').attr('disabled', true);
		  			$('#btnDisconnect').attr('disabled', false);
		  			$('#msg').attr('disabled', false);
		  			$('#msg').focus();
		  		};
	        
		  		// 메세지를 보내면 서버에 다녀온후(getBasicRemote().sendText()에서 보낸 메세지가져옴) onmessage메소드가 실행됨. 서버에서 넘긴값은 'data'변수에 담아오게된다.
	  			ws.onmessage = function (evt) {		// 서버에서 넘어온값이 '2# user명 : 메세지' 라고 한다면...(앞의 ''안의 내용은 data변수에 담겨온다.')
		  			// print('', evt.data);
		  			let index = evt.data.indexOf("#", 2);
		  			//alert("index : " + index);
		  			let no = evt.data.substring(0, 1); // '1'은 최초 접속시 user명, '2'는 메세지 보내는것, '3'은 새로운 user가 접속시..
		  			let user = evt.data.substring(2, index);
		  			
		  			// index값이 -1일 경우는 처음 접속시일 경우이다.
		  			// 메세지가 올경우는 '2#user명:메세지'로 전송되어 온다.
		  			if(index == -1) user = evt.data.substring(evt.data.indexOf("#")+1, evt.data.indexOf(":"));
		  			//alert("user : " + user);
		  			let txt = evt.data.substring(evt.data.indexOf(":")+1);
		  			//alert("txt : " + txt);
		  	   				
		  			if (no == '1') {		// 처음 접속시에는 'user'명만 들어온다.
		  				print2(user);
		  			} else if (no == '2') {	// 접속후부터는 '메세지'를 입력하고 전송할때는 '메세지'와 'user'가 함께 넘어온다.('2user명#메세지')
		  				//alert("txt : " + txt);
		  				if (txt != '') print(user, txt);	// 메세지의 내용이 있을때만 상대방의 채팅창에 출력시키게 한다.
		  			} else if (no == '3') {	// 채팅중 또 다른 사용자가 접속했을때.. '3#user'명  으로 접속된다.
		  				print3(user);
		  			}
		  			$('#list').scrollTop($('#list').prop('scrollHeight'));	// 스크롤바 가장 아래쪽으로 내리기
		  		};
	  	   	
		  		// 웹소켓이 종료될때 수행되는 메소드
		  		ws.onclose = function (evt) {
		  			console.log('소켓이 닫힙니다.');
		  		};
	
		  		// 웹소켓 에러시에 수행되는 메소드
		  		ws.onerror = function (evt) {
		  			console.log(evt.data);
		  		};
		  	} else {
		  		alert('유저명을 입력하세요.');
		  		$('#user').focus();
		  	}
		  });
		
		  // 메세지 전송 및 아이디
		  function print(user, txt) {
		  	let temp = '';
		  	if('${sNickName}'!=user) {
		  		temp += '<div style="margin-bottom:3px;margin-right:100px">';
		  	}
		  	else {
		  		/* temp += '<div style="margin-bottom:3px;" class="text-right ml-5">'; */
		  		temp += '<div style="margin-bottom:3px;margin-left:100px" class="text-right">';
		  	}
		  	temp += '<font size="0.9em">[' + user + ']</font> ';
		  	temp += '<span style="font-size:11px;color:#777;">' + new Date().toLocaleTimeString() + '</span><br/>';
		  	if('${sNickName}'!=user) {
		  		temp += '<div style="background-color:#CEF6EC;border:1px solid #fff; border-radius:4px; padding:5px; text-align:left;width:auto;">'+txt+'</div>';
		  	}
		  	else {
		  		temp += '<div style="background-color:#ff0;border:1px solid #ccc;border-radius:4px;padding:5px;text-align:left;width:auto;">'+txt+'</div>';
		  	}
		  	temp += '</div>';
			  temp = temp.replace(/\n/gi,"<br/>");	// replaceAll함수가 없기에 정규식으로 대체함.
		  			
		  	$('#list').append(temp);
		  }
		  		
		  // 다른 client 접속		
		  function print2(user) {
		  	let temp = '';
		  	temp += '<div style="margin-bottom:3px;">';
		  	temp += "<font color='red'>'" + user + "'</font> 이(가) <font color='blue'>접속</font>했습니다." ;
		  	temp += ' <span style="font-size:11px;color:#777;">' + new Date().toLocaleTimeString() + '</span>';
		  	temp += '</div>';
		  			
		  	$('#list').append(temp);
		  }
		
		  // client 접속 종료
		  function print3(user) {
		  	let temp = '';
		  	temp += '<div style="margin-bottom:3px;">';
		  	temp += "<font color='red'>'" + user + "'</font> 이(가) <font color='red'>종료</font>했습니다." ;
		  	temp += ' <span style="font-size:11px;color:#777;">' + new Date().toLocaleTimeString() + '</span>';
		  	temp += '</div>';
		  			
		  	$('#list').append(temp);
		  }
	
		  // user명 입력후 연결버튼 누를때 수행
		  $('#user').keydown(function() {
		  	if (event.keyCode == 13) {
		  		$('#btnConnect').click();
		  	}
		  });
		  
		  // 메세지 입력후 엔터키를 누를때 수행(Shift Enter키는 다음줄로 이동)
		  $('#msg').keydown(function() {
		  	if (event.keyCode == 13) {
		  		if(!event.shiftKey) {
			  		if($('#msg').val().trim() == '') return false;  // 메세지의 내용이 있을때만 수행시키게 한다.
			  		let chatColor = $("#chatColor").val();
			  		//서버에게 메시지 전달
			  		//2#유저명#메시지
			  		ws.send('2#' + $('#user').val() + '#' + $(this).val() + '@' + chatColor); //서버에게 'user명'과 '메세지'와 '컬러' 전달
			  		/* print($('#user').val(), $(this).val()); */ //본인 대화창에 'user명'과 '메세지' 출력하기
			  		print($('#user').val(), '<font color="'+chatColor+'">'+$(this).val()+'</font>'); //본인 대화창에 'user명'과 '메세지'와 '색상' 출력하기
			  		
			  		event.preventDefault();	// 커서를 원래 위치로 복원하기
			      $('#msg').val('');  		// 메세지창 청소하고 다음메세지를 준비
			  		$('#msg').focus();
			  		$('#list').scrollTop($('#list').prop('scrollHeight'));	// 스크롤바 가장 아래쪽으로 내리기
		  		}
		  	}
		  });
		  		
		  // '종료'버튼 클릭시 수행..
		  $('#btnDisconnect').click(function() {
		  	ws.send('3#' + $('#user').val() + '#');
		  	ws.close();
		  			
		  	$('#user').attr('readonly', false);
		  	
		    $('#user').val('${sNickName}');
		  	$('#user').attr('disabled', true);
		  	$('#chatStatus').html('${sNickName}님 <font color="red">접속대기</font>상태');
		  			
		  	$('#btnConnect').attr('disabled', false);
		  	$('#btnDisconnect').attr('disabled', true);
		  			
		  	$('#msg').val('');
		  	$('#msg').attr('disabled', true);
		  });
		  
	  });
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<br/>
<div class="container">
	<h2 class="page-header">대화방<font size="3" color="blue">(<span id="chatStatus">${sNickName}님 <font color="red">접속대기</font>상태</span>)</font></h2>		
	
	<div class="row">
		<div class="col-7">
		  <input type="text" name="user" value="${sNickName}" id="user" class="form-control m-0" readonly />
		</div>
		<div class="col-5">
		  <input type="button" value="연결" id="btnConnect" class="btn btn-success btn-sm m-0"/>
		  <input type="button" value="종료" id="btnDisconnect" class="btn btn-warning btn-sm m-0" disabled />
		  <input type="color" name="chatColor" id="chatColor" title="글자색 변경" style="width:40px;" class="p-0"/>
		</div>
	</div>
	<div style="height:400px;border:1px solid #fff;border-radius:4px;margin:2px 0;background-color:#F5ECCE">
		<div id="list"></div>
	</div>
	<div>
		<div>
		  <!-- <input type="text" name="msg" id="msg" placeholder="대화 내용을 입력하세요." class="form-control" disabled> -->
		  <textarea name="msg" id="msg" rows="3" placeholder="대화 내용을 입력하세요." class="form-control mb-2" disabled></textarea>
		</div>
	</div>
	
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>