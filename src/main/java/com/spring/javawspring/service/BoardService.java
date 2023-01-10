package com.spring.javawspring.service;

import java.util.List;

import com.spring.javawspring.vo.BoardVO;

public interface BoardService {

	public List<BoardVO> getBoardList(int startIdxNo, int pageSize, String part, String searchString);

	public int setBoardInput(BoardVO vo);

	public BoardVO getBoardContent(int idx);

	public void setBoardReadNum(int idx);

	public int setBoardGood(int idx);

}
