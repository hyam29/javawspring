package com.spring.javawspring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.DbProductVO;

public interface DbShopDAO {

	public DbProductVO getCategoryMainOne(@Param("categoryMainCode") String categoryMainCode, @Param("categoryMainName") String categoryMainName);

	public void setCategoryMainInput(@Param("vo") DbProductVO vo);

	public List<DbProductVO> getCategoryMain();

	public void setCategoryMainDelete(@Param("categoryMainCode") String categoryMainCode);

	public List<DbProductVO> getCategoryMiddleOne(@Param("vo") DbProductVO vo);

	public void setCategoryMiddleInput(@Param("vo") DbProductVO vo);

	public List<DbProductVO> getCategoryMiddle();

	public List<DbProductVO> getCategorySubOne(@Param("vo") DbProductVO vo);

	public void setCategoryMiddleDelete(@Param("categoryMiddleCode") String categoryMiddleCode);

	public List<DbProductVO> getCategorySub();

	public void setCategorySubInput(@Param("vo") DbProductVO vo);

	public List<DbProductVO> getDbProductOne(@Param("categorySubCode") String categorySubCode);

	public void setCategorySubDelete(@Param("categorySubCode") String categorySubCode);

	public List<DbProductVO> getCategoryMiddleName(@Param("categoryMainCode") String categoryMainCode);

	public DbProductVO getProductMaxIdx();

	public void setDbProductInput(@Param("vo") DbProductVO vo);

	public List<DbProductVO> getSubTitle();

	public List<DbProductVO> getDbShopList(@Param("part") String part);

	public List<DbProductVO> getCategorySubName(@Param("categoryMainCode") String categoryMainCode, @Param("categoryMiddleCode") String categoryMiddleCode);



}
