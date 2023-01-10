package com.spring.javawspring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.BoardVO;

public interface BoardDAO {

	public List<BoardVO> getBoardList(@Param("startIdxNo") int startIdxNo, @Param("pageSize") int pageSize, @Param("part") String part, @Param("searchString") String searchString);

	public int totRecCnt(@Param("part") String part, @Param("searchString") String searchString);

	public int setBoardInput(@Param("vo") BoardVO vo);

	public BoardVO getBoardContent(@Param("idx") int idx);

	public void setBoardReadNum(@Param("idx") int idx);

	public int setBoardGood(@Param("idx") int idx);

}
