package com.spring.javawspring.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.dao.DbShopDAO;
import com.spring.javawspring.dao.GuestDAO;
import com.spring.javawspring.dao.MemberDAO;
import com.spring.javawspring.dao.PdsDAO;
import com.spring.javawspring.dao.WebMessageDAO;

@Service
public class PageProcess {
	// 앞으로 사용하게 될 DB를 Autowired 어노테이션 해두면 모든 자료 불러올 수 있음.
	@Autowired
	GuestDAO guestDAO;
	
	@Autowired
	MemberDAO memberDAO;
	
	@Autowired
	BoardDAO boardDAO;
	
	@Autowired
	PdsDAO pdsDAO;
	
	@Autowired
	WebMessageDAO webMessageDAO;
	
	@Autowired
	DbShopDAO dbShopDAO;

	// 3번째 매개변수는 게시판 별로 사용할 것이므로 section 이라는 변수명으로 둠 (마음대로 해도 됨)
	// 4번째 매개변수 부터는 마음대로! (여기서는 part, searchString)
	public PageVO totRecCnt(int pag, int pageSize, String section, String part, String searchString) {
		PageVO pageVO = new PageVO();
		
		int totRecCnt = 0;
		
		// 게시판이 바뀐다면 DAO만 바뀌면 됨.
		if(section.equals("member")) {
			totRecCnt = memberDAO.totRecCnt(searchString);
		}
		else if(section.equals("guest")) {
			totRecCnt = guestDAO.totRecCnt();
		}
		else if(section.equals("board")) {
			totRecCnt = boardDAO.totRecCnt(part, searchString);
			//totRecCnt = boardDAO.totRecCnt();
		}
		else if(section.equals("pds")) {
			totRecCnt = pdsDAO.totRecCnt(part);
		}
		else if(section.equals("webMessage")) {
			String mid = part;
			int mSw = Integer.parseInt(searchString);
			totRecCnt = webMessageDAO.totRecCnt(mid, mSw);
		}
		else if(section.equals("dbMyOrder")) {
			String mid = part;
			totRecCnt = dbShopDAO.totRecCnt(mid);
		}
		else if(section.equals("myOrderStatus")) {
			String mid = part;
			// searchString = startJumun + "@" + endJumun + "@" + conditionOrderStatus;
			// searchString으로 넘어온 5, 6, 7번째 매개변수를 배열에 담아 넘겨줌
			String[] searchStringArr = searchString.split("@");
			totRecCnt = dbShopDAO.totRecCntMyOrderStatus(mid,searchStringArr[0],searchStringArr[1],searchStringArr[2]);
		}
		else if(section.equals("adminDbOrderProcess")) {
			String[] searchStringArr = searchString.split("@");
			totRecCnt = dbShopDAO.totRecCntAdminStatus(searchStringArr[0],searchStringArr[1],searchStringArr[2]);
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
