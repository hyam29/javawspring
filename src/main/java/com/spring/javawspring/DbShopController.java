package com.spring.javawspring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.service.DbShopService;
import com.spring.javawspring.vo.DbProductVO;

@Controller
@RequestMapping("/dbShop")
public class DbShopController {

	@Autowired
	DbShopService dbShopService;
	
	@Autowired
	PageProcess pageProcess;
	
	/* 분류 카테고리 폼 */
	@RequestMapping(value="/dbCategory", method=RequestMethod.GET)
	public String dbCategoryGet(Model model) {
		List<DbProductVO> mainVos = dbShopService.getCategoryMain();
		// Mapper에서 동적쿼리 작성을 위해 매개변수 공백 처리
		List<DbProductVO> middleVos = dbShopService.getCategoryMiddle();
		List<DbProductVO> subVos = dbShopService.getCategorySub();
		
		model.addAttribute("mainVos", mainVos);
		model.addAttribute("middleVos", middleVos);
		model.addAttribute("subVos", subVos);
		return "admin/dbShop/dbCategory";
	}
	
	/* 대분류 등록 처리 */
	@ResponseBody
	@RequestMapping(value="/categoryMainInput", method=RequestMethod.POST)
	public String categoryMainInputPost(DbProductVO vo) {
		// 기존에 동일명의 대분류가 있는지 체크
		DbProductVO imsiVo = dbShopService.getCategoryMainOne(vo.getCategoryMainCode(), vo.getCategoryMainName());
		if(imsiVo != null) return "0";
		
		// 중복X -> 대분류 저장
		dbShopService.setCategoryMainInput(vo);
		return "1";
	}
	
	/* 대분류 삭제 처리 */
	@ResponseBody
	@RequestMapping(value="/categoryMainDelete", method=RequestMethod.POST)
	public String categoryMainDeletePost(DbProductVO vo) {
		// 현재(선택된) 대분류에 속해있는 하위항목(들)이 있는지 체크
		List<DbProductVO> vos = dbShopService.getCategoryMiddleOne(vo);
		if(vos.size() != 0) return "0";
		
		// 하위항목 존재 X -> 삭제처리
		dbShopService.setCategoryMainDelete(vo.getCategoryMainCode());
		return "1";
	}
	
	/* 중분류 등록 처리 */
	@ResponseBody
	@RequestMapping(value="/categoryMiddleInput", method=RequestMethod.POST)
	public String categoryMiddleInputPost(DbProductVO vo) {
		// 기존에 동일명의 중분류가 있는지 체크
		List<DbProductVO> vos = dbShopService.getCategoryMiddleOne(vo);
		System.out.println("vos" + vos);
		if(vos.size() != 0) return "0";
		
		// 중분류 없다면 -> 중분류 저장
		dbShopService.setCategoryMiddleInput(vo);
		return "1";
	}
	
	// 중분류 삭제하기
	@ResponseBody
	@RequestMapping(value = "/categoryMiddleDelete", method = RequestMethod.POST)
	public String categoryMiddleDeletePost(DbProductVO vo) {
		// 현재 중분류가 속해있는 하위항목이 있는지를 체크한다.
		List<DbProductVO> vos = dbShopService.getCategorySubOne(vo);
		
		if(vos.size() != 0) return "0";
		dbShopService.setCategoryMiddleDelete(vo.getCategoryMiddleCode());  // 소분류항목 삭제
		
		return "1";
	}
	
	// 소분류 등록하기
	@ResponseBody
	@RequestMapping(value = "/categorySubInput", method = RequestMethod.POST)
	public String categorySubInputPost(DbProductVO vo) {
		// 기존에 같은이름의 소분류가 있는지를 체크한다.
		List<DbProductVO> vos = dbShopService.getCategorySubOne(vo);
		
		if(vos.size() != 0) return "0";
		dbShopService.setCategorySubInput(vo);  // 소분류항목 저장
		
		return "1";
	}
	
	// 소분류 삭제하기
	@ResponseBody
	@RequestMapping(value = "/categorySubDelete", method = RequestMethod.POST)
	public String categorySubDeletePost(DbProductVO vo) {
		// 현재 소분류가 속해있는 하위항목인 상품이 있는지를 체크한다.
		List<DbProductVO> vos = dbShopService.getDbProductOne(vo.getCategorySubCode());
		
		if(vos.size() != 0) return "0";
		dbShopService.setCategorySubDelete(vo.getCategorySubCode());  // 소분류항목 삭제
		
		return "1";
	}
	
	// 대분류 선택시 중분류명 가져오기
	@ResponseBody
	@RequestMapping(value="/categoryMiddleName", method = RequestMethod.POST)
	public List<DbProductVO> categoryMiddleNamePost(String categoryMainCode) {
		return dbShopService.getCategoryMiddleName(categoryMainCode);
	}
	
}
