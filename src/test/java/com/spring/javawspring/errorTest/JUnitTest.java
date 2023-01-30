package com.spring.javawspring.errorTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.After;
import org.junit.Test;

import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.MemberVO;

public class JUnitTest {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	String url = "jdbc:mysql://localhost:3306/javaworks";
	String user = "root";
	String password = "1234";
	
	public JUnitTest() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getBoard() {
		try {
			String sql = "select * from board order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			BoardVO vo = new BoardVO();
			if(rs.next()) {
				vo.setIdx(rs.getInt("idx"));
				vo.setMid(rs.getString("mid"));
				vo.setNickName(rs.getString("nickName"));
				vo.setTitle(rs.getString("title"));
			}
			System.out.println("vo : " + vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ID 검색 메소드
	@After
	public void idSearch() {
		String mid = "admin";
		MemberVO vo = getIdSearch(mid);
		System.out.println("vo : " + vo);
		
	}

	private MemberVO getIdSearch(String mid) {
		MemberVO vo = new MemberVO();
		try {
			String sql = "select * from member2 where mid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo.setIdx(rs.getInt("idx"));
				vo.setMid(rs.getString("mid"));
				vo.setNickName(rs.getString("nickName"));
				vo.setAddress(rs.getString("address"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
}
