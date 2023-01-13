package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

public interface BoardService {

	public int setBoardInput(BoardVO vo);

	public BoardVO getBoardContent(int idx);

	public void setBoardReadNum(int idx);

	public int setBoardGood(int idx);

	/* 넘어온 idx 는 partIdx이므로, 헷갈리지 않게 idx->partIdx로 변경! 여기서 변수명 변경해준다! */
	public GoodVO getBoardGoodCheck(int partIdx, String part, String mid);

	public void setGoodPlusMinus(int idx, int goodCnt);

	public ArrayList<BoardVO> getPrevNext(int idx);

	public List<BoardVO> getBoardList(int startIdxNo, int pageSize, String search, String searchString);
	//public List<BoardVO> getBoardList(int startIdxNo, int pageSize);

	/* 서버 이미지 폴더 경로 변경 및 복제 처리 */
	public void imgCheck(String content);

	/* 게시글 DB 삭제 처리 전 서버파일 삭제 처리 */
	public void imgDelete(String content);

	public void setBoardDeleteOk(int idx);

	/* 게시판 글 수정버튼 누를 시 사진파일 복사 */
	public void imgCheckUpdate(String content);

	public void setBoardUpdateOk(BoardVO vo);

	public void setGoodDBInput(GoodVO goodVo);

	public void setGoodDBDelete(int idx);

	public void setGoodUpdate(int idx, int item);

	public void setBoardReplyInput(BoardReplyVO replyVo);

	public List<BoardReplyVO> getBoardReply(int idx);

	public void setBoardReplyDeleteOk(int idx);

	public String getMaxLevelOrder(int boardIdx);

	public void setLevelOrderPlusUpdate(BoardReplyVO replyVo);

	public void setBoardReplyInput2(BoardReplyVO replyVo);

	public int setBoardSelectDelete(int checkIdx);




}
