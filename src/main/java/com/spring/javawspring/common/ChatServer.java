package com.spring.javawspring.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.websocket.Session;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chatserver")
public class ChatServer {
	// 현재 채팅 서버에 접속한 클라이언트(WebSocket Session) 목록
	// static 붙여야함!!
	private static List<Session> list = new ArrayList<Session>();
	
	private void print(String msg) {
		System.out.printf("[%tT] %s\n", Calendar.getInstance(), msg);
	}
	
	// 각각의 브라우저에서 실행시, 윈소켓 생성시에 각각의 브라우저에서는 처음 1회만 수행된다.
	@OnOpen
	public void handleOpen(Session session) {
		print("클라이언트 연결 : sessionID : " + session.getId());
		list.add(session); // 접속자 관리(****) - user명으로 접속시 모든 user들은 세션으로 저장된다.
		
		// 접속자들을 출력시켜보고 있다.(매번 새로고침하고 user명으로 들어오게되면 새로운 세션들로 생성되어 저장되게 된다.)
		/*
		for(Session sUser : list) {
			System.out.println("user : " + sUser);
		}
		*/
	}
	
	
	// 클라이언트에서 ws.send() 호출시에 무조건 아래 핸들러(handleMessage)가 감지후 수행된다.
	@OnMessage
	public void handleMessage(String msg, Session session) {
		
		// 로그인할때: '1#유저명' 으로 넘어온다.
		// 대화할때 : '2유저명#메세지' 로 넘어온다.		
		int index = msg.indexOf("#", 2);
		String no = msg.substring(0, 1); 
		String user = msg.substring(2, index);
		
		String txt = msg.substring(index + 1);
		if(txt.indexOf("@") != -1) {
			txt = txt.substring(0, txt.lastIndexOf("@"));
			//System.out.println("txt : " + txt);
			String chatColor = msg.substring(msg.lastIndexOf("@")+1);
			chatColor = chatColor.substring(1); // 색상앞에 위치한 '#'지우기
			//System.out.println("chatColor : " + chatColor);
			txt = " <font color='"+chatColor+"'>"+txt+"</font>";
		}
		if (no.equals("1")) {  // 누군가 접속했을때이다. > 1#아무개
			for (Session s : list) {
				if (s != session) { // 현재 접속자가 아닌 나머지 사람들
					try {
						s.getBasicRemote().sendText("1#" + user + "#");  // 접속된 다른 user들에게 현재접속자(user)를 알려주게 한다.
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		} else if (no.equals("2")) {  // 접속된 누군가가 '메세지'를 전송할때(대화할때 : '2유저명#메세지' 로 넘어온다.)
			for (Session s : list) {
				if (s != session) { // 현재 접속자(메세지를 보낸 접속자)가 아닌 나머지 접속자들한테 아래 메세지를 보낼 수 있도록 한다.
					try {
						s.getBasicRemote().sendText("2#" + user + ":" + txt);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			} 
		} else if (no.equals("3")) { // 종료버튼 클릭시 > 3#아무개
			for (Session s : list) {
				if (s != session) { // 현재 접속자가 아닌 나머지 접속자들에게 현재 접속한 사용자를 띄워주게 한다.
					try {
						s.getBasicRemote().sendText("3#" + user + "#");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			list.remove(session);	// 클라이언트가 최초 서버에 접속시 세션이 저장되어 있었기에 종료시 생성되어 있던 세션을 삭제한다.
		}
		
	}
	
	@OnClose
	public void handleClose(Session session) {
		System.out.println("Websocket Close");
		list.remove(session);	// 현재 생성된 세션을 List객체에서 제거시킨다.
	}
	
	@OnError
	public void handleError(Throwable t) {
		
	}
}
