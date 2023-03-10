package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.BoardReplyVO;
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

	public void setGoodDBInput(@Param("goodVo") GoodVO goodVo);

	public void setGoodDBDelete(@Param("idx") int idx);

	public void setGoodUpdate(@Param("idx") int idx, @Param("item") int item);

	public void setBoardReplyInput(@Param("replyVo") BoardReplyVO replyVo);

	public List<BoardReplyVO> getBoardReply(@Param("idx") int idx);

	public void setBoardReplyDeleteOk(@Param("idx") int idx);

	public String getMaxLevelOrder(@Param("boardIdx") int boardIdx);

	public void setLevelOrderPlusUpdate(@Param("replyVo") BoardReplyVO replyVo);

	public void setBoardReplyInput2(@Param("replyVo") BoardReplyVO replyVo);

	public int setBoardSelectDelete(@Param("idx") int checkIdx);


}
