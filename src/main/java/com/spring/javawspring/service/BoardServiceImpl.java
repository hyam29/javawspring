package com.spring.javawspring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardDAO boardDAO;
	
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

	@Override
	public GoodVO getBoardGoodCheck(int partIdx, String part, String mid) {
		return boardDAO.getBoardGoodCheck(partIdx, part, mid);
	}

	@Override
	public void setGoodPlusMinus(int idx, int goodCnt) {
		boardDAO.setGoodPlusMinus(idx, goodCnt);
		
	}

	@Override
	public ArrayList<BoardVO> getPrevNext(int idx) {
		return boardDAO.getPrevNext(idx);
	}
	
	@Override
	public List<BoardVO> getBoardList(int startIdxNo, int pageSize, String search, String searchString) {
		return boardDAO.getBoardList(startIdxNo, pageSize, search, searchString);
	}


	/* input 시에 서버폴더에 저장된 이미지 경로 수정 (작업폴더(ckeditor)와 실제저장폴더(board) 구분) */
	@Override
	public void imgCheck(String content) {
		
		//			0					1					2					3					4
		//      01234567890123456789012345678901234567890
		// <img src="/javawspring/data/ckeditor/230110164523_숨은작품찾기+(1).jpg" => 32번 index부터 그림파일 이름 시작
		// content안에 그림파일이 존재할 때만 작업을 수행하도록 처리 (DB저장된 공통된 이름 중 src="/~~~만 뽑아서 비교)
		if(content.indexOf("src=\"/") == -1) return;
		
		// request 객체를 사용하기 위해 아래와 같이 선언
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/");
		
		// 폴더명이 바뀌면 시작위치가 변경 -> position 번호만 변경하면 됨!
		int position = 32;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\"")); // 그림 파일명 꺼내는 작업
			 
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = uploadPath + "board/" + imgFile; // pds게시판에서 사용하기 위해서 board/ -> pds/ 로 변경하면 됨.
			
			fileCopyCheck(origFilePath, copyFilePath); // board폴더에서 파일을 복사하고자 함.
			// 여기까지 파일 복사 처리 완료
			
			// 다음 이미지가 있다면, 반복처리를 위한 조건문
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
	}
	// 서버->서버로 파일 복사 (fileinput, fileoutput Stream 모두 필요) 메소드 생성
	private void fileCopyCheck(String origFilePath, String copyFilePath) {
		File origFile = new File(origFilePath);
		File copyFile = new File(copyFilePath);
		
		// 오버라이딩 되어있으므로 예외처리 throw 할 수 없어서 여기서 예외처리 해야 함!
		try {
			FileInputStream fis = new FileInputStream(origFile);
			FileOutputStream fos = new FileOutputStream(copyFile);
			//FileOutputStream fos = new FileOutputStream(new File()); // copyFile 안에 File 선언하면서 생성해도 됨
			
			byte[] buffer = new byte[2048];
			int cnt = 0;
			
			// 파일 복사 완료 처리 (추가 예외처리 해야 함)
			while((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}
			fos.flush();
			fos.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 게시글 삭제 전 서버파일 삭제 처리 -> position 변경!!!!!!!!!!!!!!!!
	@Override
	public void imgDelete(String content) {
		//			0					1					2					3					4
		//      01234567890123456789012345678901234567890
		// <img src="/javawspring/data/ckeditor/board/230110164523_숨은작품찾기+(1).jpg" => 38번 index부터 그림파일 이름 시작
		// content안에 그림파일이 존재할 때만 작업을 수행하도록 처리 (DB저장된 공통된 이름 중 src="/~~~만 뽑아서 비교)
		if(content.indexOf("src=\"/") == -1) return;
		
		// request 객체를 사용하기 위해 아래와 같이 선언
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/");
		
		// 폴더명이 바뀌면 시작위치가 변경 -> position 번호만 변경하면 됨!
		int position = 38;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\"")); // 그림 파일명 꺼내는 작업
			 
			String origFilePath = uploadPath + imgFile;
			
			fileDelete(origFilePath); // board폴더에서 파일들을 삭제하고자 함
			// 여기까지 파일 복사 처리 완료
			
			// 다음 이미지가 있다면, 반복처리를 위한 조건문
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
	}
	// 파일 삭제 메소드 생성
	private void fileDelete(String origFilePath) {
		File delFile = new File(origFilePath);
		// 파일 존재해? -> 지워줘!
		if(delFile.exists()) delFile.delete();
	}

	/* DB 게시글 삭제 처리 */
	@Override
	public void setBoardDeleteOk(int idx) {
		boardDAO.setBoardDeleteOk(idx);
	}

	/* 수정 전 원본사진파일 복사 */
	@Override
	public void imgCheckUpdate(String content) {
		
		//			0					1					2					3					4
		//      01234567890123456789012345678901234567890
		// <img src="/javawspring/data/ckeditor/board/230110164523_숨은작품찾기+(1).jpg" => 38번 index부터 그림파일 이름 시작 (원본)
		// content안에 그림파일이 존재할 때만 작업을 수행하도록 처리 (DB저장된 공통된 이름 중 src="/~~~만 뽑아서 비교)
		if(content.indexOf("src=\"/") == -1) return;
		
		// request 객체를 사용하기 위해 아래와 같이 선언
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/");
		
		// 폴더명이 바뀌면 시작위치가 변경 -> position 번호만 변경하면 됨!
		int position = 38;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\"")); // 그림 파일명 꺼내는 작업
			 
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/" + imgFile);
			
			fileCopyCheck(origFilePath, copyFilePath); // board폴더에서 파일을 ckeditor폴더로 복사.
			// 여기까지 파일 복사 처리 완료
			
			// 다음 이미지가 있다면, 반복처리를 위한 조건문
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
	}

	@Override
	public void setBoardUpdateOk(BoardVO vo) {
		boardDAO.setBoardUpdateOk(vo);
		
	}

	@Override
	public void setGoodDBInput(GoodVO goodVo) {
		boardDAO.setGoodDBInput(goodVo);
		
	}

	@Override
	public void setGoodDBDelete(int idx) {
		boardDAO.setGoodDBDelete(idx);
		
	}

	@Override
	public void setGoodUpdate(int idx, int item) {
		boardDAO.setGoodUpdate(idx, item);
		
	}

	@Override
	public void setBoardReplyInput(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput(replyVo);
	}

	@Override
	public List<BoardReplyVO> getBoardReply(int idx) {
		return boardDAO.getBoardReply(idx);
	}

	@Override
	public void setBoardReplyDeleteOk(int idx) {
		boardDAO.setBoardReplyDeleteOk(idx);
	}

	@Override
	public String getMaxLevelOrder(int boardIdx) {
		return boardDAO.getMaxLevelOrder(boardIdx);
	}

	@Override
	public void setLevelOrderPlusUpdate(BoardReplyVO replyVo) {
		boardDAO.setLevelOrderPlusUpdate(replyVo);
	}

	@Override
	public void setBoardReplyInput2(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput2(replyVo);
	}

	@Override
	public int setBoardSelectDelete(int checkIdx) {
		return boardDAO.setBoardSelectDelete(checkIdx);
	}



	
}
