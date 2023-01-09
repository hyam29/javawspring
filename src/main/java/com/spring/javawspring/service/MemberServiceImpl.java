package com.spring.javawspring.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.MemberDAO;
import com.spring.javawspring.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	MemberDAO memberDAO;

	@Override
	public MemberVO getMemberIdCheck(String mid) {
		return memberDAO.getMemberIdCheck(mid);
	}

	@Override
	public MemberVO getMemberNickNameCheck(String nickName) {
		return memberDAO.getMemberNickNameCheck(nickName);
	}

	@Override
	public int setMemberJoinOk(MemberVO vo) {
		return memberDAO.setMemberJoinOk(vo);
	}

	@Override
	public void setMemberVisitProcess(MemberVO vo) {
		// DB로 보내는 것이 아니기 떄문에, memberDAO X
		Date now = new Date(); // java.util 에 들어있는 객체 Date 생성해야 함!
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strNow = sdf.format(now);
		
		// 오늘 처음 방문한 사람(마지막 방문일 != 현재 날짜) -> 오늘방문카운트(todayCnt) 를 0으로 셋팅
		if(!vo.getLastDate().substring(0,10).equals(strNow)) {
			// memberDAO.setTodayCntUpdate(vo.getMid());
			vo.setTodayCnt(0);
		}
		
		int todayCnt = vo.getTodayCnt() + 1;
		
		// 당일 재방문한 사람 -> '총방문수', '오늘방문수', '포인트' 누적 (5번 이상 방문시 누적X)
		int nowTodayPoint = 0;
		if(vo.getTodayCnt() >= 5) {
			nowTodayPoint = vo.getPoint();
		}
		else {
			nowTodayPoint = vo.getPoint() + 10;
		}
		// 당일 방문(tocnt)이 5번 이상이라면, 기존방문포인트를(방문포인트 변수(nowTodayPoint)에 넣어서 그것만 sql에서 누적시켜서 넘겨주면 됨) 그대로 보내기 -> sql 구문 바껴야 함
		memberDAO.setMemTotalUpdate(vo.getMid(), nowTodayPoint, todayCnt);
		
	}

	@Override
	public int totRecCnt() {
		return memberDAO.totRecCnt();
	}

	@Override
	public List<MemberVO> getMemberList(int startIdxNo, int pageSize) {
		return memberDAO.getMemberList(startIdxNo, pageSize);
	}

	@Override
	public int totTermRecCnt(String mid) {
		return memberDAO.totTermRecCnt(mid);
	}

	@Override
	public ArrayList<MemberVO> getTermMemberList(int startIdxNo, int pageSize, String mid) {
		return memberDAO.getTermMemberList(startIdxNo, pageSize, mid);
	}
	
}
