package com.spring.javawspring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.DbBaesongVO;
import com.spring.javawspring.vo.DbCartVO;
import com.spring.javawspring.vo.DbOptionVO;
import com.spring.javawspring.vo.DbOrderVO;
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

	public DbProductVO getDbShopProduct(@Param("idx") int idx);

	public List<DbOptionVO> getDbShopOption(@Param("productIdx") int productIdx);

	public DbProductVO getProductInfor(@Param("productName") String productName);

	public List<DbProductVO> getCategoryProductName(@Param("categoryMainCode") String categoryMainCode, @Param("categoryMiddleCode") String categoryMiddleCode, @Param("categorySubCode") String categorySubCode);

	public List<DbOptionVO> getOptionList(@Param("productIdx") int productIdx);

	public int getOptionSame(@Param("productIdx") int productIdx, @Param("optionName") String optionName);

	public void setDbOptionInput(@Param("vo") DbOptionVO vo);

	public void setOptionDelete(@Param("idx") int idx);

	public DbCartVO getDbCartProductOptionSearch(@Param("productName") String productName, @Param("optionName") String optionName, @Param("mid") String mid);

	public void setDbShopCartUpdate(@Param("vo") DbCartVO vo);

	public void setDbShopCartInput(@Param("vo") DbCartVO vo);

	public List<DbCartVO> getDbCartList(@Param("mid") String mid);

	public void dbCartDelete(@Param("idx") int idx);

	public DbOrderVO getOrderMaxIdx();

	public DbCartVO getCartIdx(@Param("idx") int idx);

	public void setDbOrder(@Param("vo") DbOrderVO vo);

	public void dbCartDeleteAll(@Param("idx") int idx);

	public void setDbBaesong(@Param("baesongVo") DbBaesongVO baesongVo);

	public void setMemberPointPlus(@Param("point") int point, @Param("mid") String mid);

	public int totRecCnt(@Param("mid") String mid);

	public List<DbProductVO> getMyOrderList(@Param("startIdxNo") int startIdxNo, @Param("pageSize") int pageSize, @Param("mid") String mid);

	// 페이징처리 매개변수로 넘어온 변수명들 모두 수정 필수!
	public int totRecCntMyOrderStatus(@Param("mid") String mid, @Param("startJumun") String startJumun, @Param("endJumun") String endJumun, @Param("conditionOrderStatus") String conditionOrderStatus);

	public List<DbBaesongVO> getMyOrderStatus(@Param("startIdxNo") int startIdxNo, @Param("pageSize") int pageSize, @Param("mid") String mid, 
			@Param("startJumun") String startJumun, @Param("endJumun") String endJumun, @Param("conditionOrderStatus") String conditionOrderStatus);

	public int totRecCntAdminStatus(@Param("startJumun") String startJumun, @Param("endJumun") String endJumun, @Param("orderStatus") String orderStatus);

	public List<DbBaesongVO> getAdminOrderStatus(@Param("startIdxNo") int startIdxNo, @Param("pageSize") int pageSize, @Param("startJumun") String startJumun, @Param("endJumun") String endJumun, @Param("orderStatus") String orderStatus);

	public void setOrderStatusUpdate(@Param("orderIdx") String orderIdx, @Param("orderStatus") String orderStatus);


}
