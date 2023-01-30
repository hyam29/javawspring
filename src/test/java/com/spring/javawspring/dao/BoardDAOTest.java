package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

public class BoardDAOTest {

	@Test
	public void testGetBoardList() {
		BoardDAO dao = new BoardDAO() {
			
			@Override
			public int totRecCnt(String part, String searchString) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void setLevelOrderPlusUpdate(BoardReplyVO replyVo) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGoodUpdate(int idx, int item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGoodPlusMinus(int idx, int goodCnt) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGoodDBInput(GoodVO goodVo) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGoodDBDelete(int idx) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBoardUpdateOk(BoardVO vo) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int setBoardSelectDelete(int checkIdx) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void setBoardReplyInput2(BoardReplyVO replyVo) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBoardReplyInput(BoardReplyVO replyVo) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBoardReplyDeleteOk(int idx) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBoardReadNum(int idx) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int setBoardInput(BoardVO vo) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int setBoardGood(int idx) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void setBoardDeleteOk(int idx) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public ArrayList<BoardVO> getPrevNext(int idx) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getMaxLevelOrder(int boardIdx) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<BoardReplyVO> getBoardReply(int idx) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<BoardVO> getBoardList(int startIdxNo, int pageSize, String search, String searchString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public GoodVO getBoardGoodCheck(int partIdx, String part, String mid) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoardVO getBoardContent(int idx) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		List<BoardVO> vos = dao.getBoardList(0, 10, "", "");
		System.out.println("vos : " + vos);
	}

}
