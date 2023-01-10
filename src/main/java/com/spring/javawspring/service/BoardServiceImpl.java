package com.spring.javawspring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.vo.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardDAO boardDAO;

	@Override
	public List<BoardVO> getBoardList(int startIdxNo, int pageSize, String part, String searchString) {
		return boardDAO.getBoardList(startIdxNo, pageSize, part, searchString);
	}

	@Override
	public int setBoardInput(BoardVO vo) {
		return boardDAO.setBoardInput(vo);
	}

	@Override
	public BoardVO getBoardContent(int idx) {
		return boardDAO.getBoardContent(idx);
	}

	@Override
	public void setBoardReadNum(int idx) {
		boardDAO.setBoardReadNum(idx);
		
	}

	@Override
	public int setBoardGood(int idx) {
		return boardDAO.setBoardGood(idx);
	}
}
