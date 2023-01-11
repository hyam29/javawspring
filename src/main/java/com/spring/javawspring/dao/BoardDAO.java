package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

public interface BoardDAO {

	public int totRecCnt(@Param("search") String part, @Param("searchString") String searchString);
	
	public List<BoardVO> getBoardList(@Param("startIdxNo") int startIdxNo, @Param("pageSize") int pageSize, @Param("search") String search, @Param("searchString") String searchString);
	
	//public int totRecCnt();

	//public List<BoardVO> getBoardList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize);
	
	public int setBoardInput(@Param("vo") BoardVO vo);

	public BoardVO getBoardContent(@Param("idx") int idx);

	public void setBoardReadNum(@Param("idx") int idx);

	public int setBoardGood(@Param("idx") int idx);

	public GoodVO getBoardGoodCheck(@Param("partIdx") int partIdx, @Param("part") String part, @Param("mid") String mid);

	public void setGoodPlusMinus(@Param("idx") int idx, @Param("goodCnt") int goodCnt);

	public ArrayList<BoardVO> getPrevNext(@Param("idx") int idx);

	public void setBoardDeleteOk(@Param("idx") int idx);

	public void setBoardUpdateOk(@Param("vo") BoardVO vo);

	//public int totRecCnt(String part, String searchString);


}
