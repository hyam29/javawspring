package com.spring.javawspring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.service.DbShopService;
import com.spring.javawspring.vo.DbOptionVO;
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
	
	/* 상품등록을 위한 폼 */
	@RequestMapping(value = "/dbProduct", method = RequestMethod.GET)
	public String dbProductGet(Model model) {
		// 메인카테고리의 목록리스트 가져오기 위해 카테고리 폼에서 작성된 vos 복사붙여넣기
		List<DbProductVO> mainVos = dbShopService.getCategoryMain();
		model.addAttribute("mainVos", mainVos);
		
		return "admin/dbShop/dbProduct";
	}
	
	/* 중분류 선택시 소분류항목들 가져오기 */
	@ResponseBody
	@RequestMapping(value = "/categorySubName", method = RequestMethod.POST)
	public List<DbProductVO> categorySubNamePost(String categoryMainCode, String categoryMiddleCode) {
		
		return dbShopService.getCategorySubName(categoryMainCode, categoryMiddleCode);
	}
	
	/* 소분류 선택시 상품명(모델명) 가져오기 */
	@ResponseBody
	@RequestMapping(value = "/categoryProductName", method = RequestMethod.POST)
	public List<DbProductVO> categoryProductNamePost(String categoryMainCode, String categoryMiddleCode, String categorySubCode) {
		
		return dbShopService.getCategoryProductName(categoryMainCode, categoryMiddleCode, categorySubCode);
	}
	
	/* 관리자 상품등록 시 ckeditor에 그림을 서버에 올린다면 dbShop폴더에 저장되고, 저장된 파일을 웹브라우저 textarea에 보여줌 */
	@ResponseBody
	@RequestMapping("/imageUpload")
	public void imageUploadGet(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String originalFilename = upload.getOriginalFilename();
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		originalFilename = sdf.format(date) + "_" + originalFilename;
		
		byte[] bytes = upload.getBytes();
		
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/dbShop/");
		OutputStream outStr = new FileOutputStream(new File(uploadPath + originalFilename));
		outStr.write(bytes);
		
		PrintWriter out = response.getWriter();
		String fileUrl = request.getContextPath() + "/data/dbShop/" + originalFilename;
		out.println("{\"originalFilename\":\""+originalFilename+"\",\"uploaded\":1,\"url\":\""+fileUrl+"\"}");
		
		out.flush();
		outStr.close();
	}
	
	/* 상품등록 처리 */
	// 사진이 하나이므로, MultipartFile 객체 사용 (여러개라면, HttpMultipartFile ...?)
	@RequestMapping(value = "/dbProduct", method = RequestMethod.POST)
	public String dbProductPost(MultipartFile file, DbProductVO vo) {
		// (service에서 처리) 이미지파일 업로드 시 ckeditor폴더에서 product폴더로 복사작업 처리 ('dbShop'폴더에서 -> 'dbShop/product'폴더로)
		dbShopService.imgCheckProductInput(file, vo);
		
		return "redirect:/msg/dbProductInputOk";
	}
	
	/* 등록된 상품 모두 보여주기(관리자화면용) - 상품 소분류명(subTitle)도 함께 출력 */
	@RequestMapping(value = "/dbShopList", method=RequestMethod.GET)
	public String dbShopListGet(Model model,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part) {
		// 소분류명 가져오기
		List<DbProductVO> subTitleVos = dbShopService.getSubTitle();
		model.addAttribute("subTitleVos", subTitleVos);
		model.addAttribute("part", part);
		
		// 전체 상품리스트 가져오기
		List<DbProductVO> productVos = dbShopService.getDbShopList(part);
		model.addAttribute("productVos", productVos);
		
		return "admin/dbShop/dbShopList";
	}
	
	/* 관리자에서 진열된 상품을 클릭하였을 경우 해당 상품의 상세내역 보여주기 */
	@RequestMapping(value = "/dbShopContent", method = RequestMethod.GET)
	public String dbShopContentGet(Model model, int idx) {
		// 1건(vo)의 상품 정보 가져오기
		DbProductVO productVo = dbShopService.getDbShopProduct(idx); 
		// 모든 옵션(vos) 정보 가져오기 (여기서의 idx는 옵션의 idx XXX, 상품의 idx(productIdx) -> service에서 변수명수정)
		List<DbOptionVO> optionVos = dbShopService.getDbShopOption(idx);
		
		model.addAttribute("productVo", productVo);
		model.addAttribute("optionVos", optionVos);
		
		return "admin/dbShop/dbShopContent";
	}

	/* 관리자 상품 옵션 등록창 */
	@RequestMapping(value = "/dbOption", method = RequestMethod.GET)
	public String dbOptionGet(Model model) {
		List<DbProductVO> mainVos = dbShopService.getCategoryMain();
		model.addAttribute("mainVos", mainVos);
//		String[] productNames = dbShopService.getProductName();
//		model.addAttribute("productNames", productNames);
		
		return "admin/dbShop/dbOption";
	}
	
	/* 옵션등록창에서 상품 선택 시, 선택된 상품의 상세설명 가져오기 */
	@ResponseBody
	@RequestMapping(value="/getProductInfor", method = RequestMethod.POST)
	public DbProductVO getProductInforPost(String productName) {
		return dbShopService.getProductInfor(productName);
	}
	
	/* 옵션등록창에서 '옵션보기' 버튼 클릭 시, 해당 제품의 모든 옵션 보여주기 */
	@ResponseBody
	@RequestMapping(value="/getOptionList", method = RequestMethod.POST)
	public List<DbOptionVO> getOptionListPost(int productIdx) {
		// getProductInfor와 동일하므로 아래 작성 안해도 됨
		return dbShopService.getOptionList(productIdx);
	}	
	
	/* 옵션 기록사항 등록 처리 */
	@RequestMapping(value="/dbOption", method=RequestMethod.POST)
	public String dbOptionPost(DbOptionVO vo, String[] optionName, int[] optionPrice) {
		for(int i=0; i<optionName.length; i++) {
			// 옵션 등록 시 동일명칭(중복방지) 사용 체크
			// getOptionSame 생성시 반복문을 돌리므로 optionName은 String 타입의 string 변수명으로 service에 넘어감 -> 수정필요
			int optionCnt = dbShopService.getOptionSame(vo.getProductIdx(), optionName[i]);
			
			if(optionCnt != 0) continue;
			vo.setProductIdx(vo.getProductIdx());
			vo.setOptionName(optionName[i]);
			vo.setOptionPrice(optionPrice[i]);
			
			dbShopService.setDbOptionInput(vo);
		}
		return "redirect:/msg/dbOptionInputOk";
	}
	
	/* 옵션 삭제 처리 */
	@ResponseBody
	@RequestMapping(value="/optionDelete", method = RequestMethod.POST)
	public String optionDeletePost(int idx) {
		dbShopService.setOptionDelete(idx);
		return "";
	}
	
	// 여기까지 관리자
	/*********************************************************************************************************************************/
	// 아래로 사용자(고객)
	
	/* 등록된 상품 보여주기(사용자(고객)화면에서 보여주기) */
	@RequestMapping(value="/dbProductList", method = RequestMethod.GET)
	public String dbProductListGet(
			@RequestParam(name="part", defaultValue="전체", required=false) String part,
			Model model) {
		List<DbProductVO> subTitleVos = dbShopService.getSubTitle();
		model.addAttribute("subTitleVos", subTitleVos);
		model.addAttribute("part", part);
		
		List<DbProductVO> productVos = dbShopService.getDbShopList(part);
		model.addAttribute("productVos", productVos);
		return "dbShop/dbProductList";
	}
	
	/* 진열된 상품클릭 시 해당상품의 상세정보 보여주기 */
	@RequestMapping(value="/dbProductContent", method=RequestMethod.GET)
	public String dbProductContentGet(int idx, Model model) {
		DbProductVO productVo = dbShopService.getDbShopProduct(idx);
		List<DbOptionVO> optionVos = dbShopService.getDbShopOption(idx);
		
		model.addAttribute("productVo", productVo);
		model.addAttribute("optionVos", optionVos);
		
		return "dbShop/dbProductContent";
	}

}
