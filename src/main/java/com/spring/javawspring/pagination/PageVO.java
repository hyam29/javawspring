package com.spring.javawspring.pagination;

import lombok.Data;

@Data
public class PageVO {
	/* 기본 pagination에만 필요한 변수 */
	private int pag;
	private int pageSize;
	private int totRecCnt;
	private int totPage;
	private int startIdxNo;
	private int curScrStartNo;
	private int blockSize;
	private int curBlock;
	private int lastBlock;
	
	/* 넘겨줘야(추가) 할 변수가 있다면 아래에 생성(ex) PDS에서 part) */
	private String part;
}
