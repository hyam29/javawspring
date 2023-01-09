package com.spring.javawspring.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.GuestVO;

public interface StudyDAO {

	GuestVO getGuestMid(@Param("name") String mid);

	ArrayList<GuestVO> getGuestNames(@Param("name") String mid);

	ArrayList<GuestVO> getGuestPart(@Param("part") String part, @Param("name") String mid);

}
