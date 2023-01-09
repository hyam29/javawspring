package com.spring.javawspring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.GuestVO;

public interface GuestDAO {

	public List<GuestVO> getGuestList(@Param("startIdxNo") int startIdxNo, @Param("pageSize") int pageSize);

	public void setGuestInput(@Param("vo") GuestVO vo);

	public int totRecCnt();

	public void setGuestDelete(@Param("idx") int idx);
	
}
