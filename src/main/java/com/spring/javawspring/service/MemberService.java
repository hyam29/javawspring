package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import com.spring.javawspring.vo.MemberVO;

public interface MemberService {

	public MemberVO getMemberIdCheck(String mid);

	public MemberVO getMemberNickNameCheck(String nickName);

	public int setMemberJoinOk(MemberVO vo);

	public void setMemberVisitProcess(MemberVO vo);

	public int totRecCnt();

	public List<MemberVO> getMemberList(int startIdxNo, int pageSize);

	public int totTermRecCnt(String mid);

	public ArrayList<MemberVO> getTermMemberList(int startIdxNo, int pageSize, String mid);

}
