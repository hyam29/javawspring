package com.spring.javawspring.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.GuestDAO;
import com.spring.javawspring.dao.MemberDAO;

@Service
public class PageProcess {
	// 앞으로 사용하게 될 DB를 Autowired 어노테이션 해두면 모든 자료 불러올 수 있음.
	@Autowired
	GuestDAO guestDAO;
	
	@Autowired
	MemberDAO memberDAO;

	// 3번째 매개변수는 게시판 별로 사용할 것이므로 section 이라는 변수명으로 둠 (마음대로 해도 됨)
	// 4번째 매개변수 부터는 마음대로! (여기서는 part, searchString)
	public PageVO totRecCnt(int pag, int pageSize, String section, String part, String searchString) {
		PageVO pageVO = new PageVO();
		
		int totRecCnt = 0;
		
		// 게시판이 바뀐다면 DAO만 바뀌면 됨.
		if(section.equals("member")) {
			totRecCnt = memberDAO.totRecCnt();
		}
		else if(section.equals("guest")) {
			totRecCnt = guestDAO.totRecCnt();
		}
		
		/* 페이징처리 */
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1;
		int startIdxNo = (pag - 1) * pageSize;
		int curScrStartNo = totRecCnt - startIdxNo;
		
		/* 블록 페이징 처리 (3단계) => 블록의 시작번호를 0번부터 처리 */
		int blockSize = 3;
		int curBlock = (pag - 1) / blockSize;
		int lastBlock = (totPage - 1) / blockSize;
		
		pageVO.setPag(pag);
		pageVO.setPageSize(pageSize);
		pageVO.setTotRecCnt(totRecCnt);
		pageVO.setTotPage(totPage);
		pageVO.setStartIdxNo(startIdxNo);
		pageVO.setCurScrStartNo(curScrStartNo);
		pageVO.setBlockSize(blockSize);
		pageVO.setCurBlock(curBlock);
		pageVO.setLastBlock(lastBlock);
		
		return pageVO;
	}
	
	
}
