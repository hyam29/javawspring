package com.spring.javawspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.AdminDAO;

@Service // class로 생성시에는 어노테이션 다름!!!
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminDAO adminDAO;

	@Override
	public int setMemberLevelCheck(int idx, int level) {
		return adminDAO.setMemberLevelCheck(idx, level);
	}

	@Override
	public int setAdminMemberDel(int idx) {
		return adminDAO.setAdminMemberDel(idx);
		
	}
}
