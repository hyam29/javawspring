package com.spring.javawspring.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.vo.DbProductVO;

public interface DbShopService {

	public DbProductVO getCategoryMainOne(String categoryMainCode, String categoryMainName);

	public void setCategoryMainInput(DbProductVO vo);

	public List<DbProductVO> getCategoryMain();

	public void setCategoryMainDelete(String categoryMainCode);

	public List<DbProductVO> getCategoryMiddleOne(DbProductVO vo);

	public void setCategoryMiddleInput(DbProductVO vo);

	public List<DbProductVO> getCategoryMiddle();

	public List<DbProductVO> getCategorySubOne(DbProductVO vo);

	public void setCategoryMiddleDelete(String categoryMiddleCode);

	public List<DbProductVO> getCategorySub();

	public void setCategorySubInput(DbProductVO vo);

	public List<DbProductVO> getDbProductOne(String categorySubCode);

	public void setCategorySubDelete(String categorySubCode);

	public List<DbProductVO> getCategoryMiddleName(String categoryMainCode);

	public void imgCheckProductInput(MultipartFile file, DbProductVO vo);

	public List<DbProductVO> getSubTitle();

	public List<DbProductVO> getDbShopList(String part);

	public List<DbProductVO> getCategorySubName(String categoryMainCode, String categoryMiddleCode);
	


}
