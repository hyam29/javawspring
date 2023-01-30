package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.vo.MemberVO;

public interface MemberService {

	public MemberVO getMemberIdCheck(String mid);

	public MemberVO getMemberNickNameCheck(String nickName);

	public int setMemberJoinOk(MultipartFile fName, MemberVO vo);

	public void setMemberVisitProcess(MemberVO vo);

//	public int totRecCnt();

	public List<MemberVO> getMemberList(int startIdxNo, int pageSize, String mid);

	public int totTermRecCnt(String mid);

	public ArrayList<MemberVO> getTermMemberList(int startIdxNo, int pageSize, String mid);

	public void setMemberPwdUpdate(String mid, String pwd);

	public void setMemberDelete(String mid);

	public MemberVO getMemberIdSearch(String email);

	public int getMonthNewUser();
	
	public MemberVO getMemberNickNameEmailCheck(String nickName, String email);

	public void setKakaoMemberInputOk(String mid, String pwd, String nickName, String email);

	public void setMemberUserDelCheck(String mid);

	public int setMemberUpdate(MultipartFile fName, MemberVO vo, String mid);


}
