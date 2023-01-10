package com.spring.javawspring.vo;

import lombok.Data;

@Data
public class BoardVO {
	private int idx;
	private String nickName;
	private String title;
	private String email;
	private String homePage;
	private String content;
	private String wDate;
	private String hostIp;
	private int readNum;
	private int good;
	private String mid;
	
	/* 위의 DB 설계된 필드와 아래 프로그램 필드를 꼭 구분(띄어쓰기)해서 관리해주기 */
	
	private int day_diff; // 날짜 차이 계산 필드 (1일차이 계산)
	private int hour_diff; // 날짜 차이 계산 필드 (1일차이 계산)
	
	/* 이전글/다음글을 위한 변수 설정 */
	private int preIdx;
	private int nextIdx;
	private String preTitle;
	private String nextTitle;
	
	/* 댓글의 갯수를 저장하기 위한 변수 설정 */
	private int replyCnt;

	
}

