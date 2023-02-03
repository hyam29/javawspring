package com.spring.javawspring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.DbShopService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.DbBaesongVO;
import com.spring.javawspring.vo.DbCartVO;
import com.spring.javawspring.vo.DbOptionVO;
import com.spring.javawspring.vo.DbOrderVO;
import com.spring.javawspring.vo.DbProductVO;
import com.spring.javawspring.vo.MemberVO;
import com.spring.javawspring.vo.PayMentVO;

@Controller
@RequestMapping("/dbShop")
public class DbShopController {

	@Autowired
	DbShopService dbShopService;
	
	@Autowired
	PageProcess pageProcess;
	
	@Autowired
	MemberService memberService;
	
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
	
	// 고객이 주문한 상품 확인
	@RequestMapping(value="/adminOrderStatus")
	public String dbOrderProcessGet(Model model,
   @RequestParam(name="startJumun", defaultValue="", required=false) String startJumun,
   @RequestParam(name="endJumun", defaultValue="", required=false) String endJumun,
   @RequestParam(name="orderStatus", defaultValue="전체", required=false) String orderStatus,
   @RequestParam(name="pag", defaultValue="1", required=false) int pag,
   @RequestParam(name="pageSize", defaultValue="5", required=false) int pageSize) {
		
		List<DbBaesongVO> vos = null;
		PageVO pageVo = null;
		String strNow = "";
		if(startJumun.equals("")) {
			Date now = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    strNow = sdf.format(now);
	    
	    startJumun = strNow;
	    endJumun = strNow;
		}
   
	 // 페이징처리 추가된 매개변수 합쳐서 넘김
   String strOrderStatus = startJumun + "@" + endJumun + "@" + orderStatus;
   pageVo = pageProcess.totRecCnt(pag, pageSize, "adminDbOrderProcess", "", strOrderStatus);

		vos = dbShopService.getAdminOrderStatus(pageVo.getStartIdxNo(), pageSize, startJumun, endJumun, orderStatus);
	
	  model.addAttribute("startJumun", startJumun);
	  model.addAttribute("endJumun", endJumun);
	  model.addAttribute("orderStatus", orderStatus);
	  model.addAttribute("vos", vos);
	  model.addAttribute("pageVo", pageVo);
	
	  return "admin/dbShop/dbOrderProcess";
	}
	
	/* 주문관리에서 주문상태 변경(배송중, 배송완료 등) 처리 (ajax) */
	@ResponseBody
	@RequestMapping(value="/goodsStatus", method=RequestMethod.POST)
	public String goodsStatusGet(String orderIdx, String orderStatus) {
		dbShopService.setOrderStatusUpdate(orderIdx, orderStatus);
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
	
	/* 상품상세정보창에서 '장바구니' or '주문하기' 버튼 클릭 시, 모두 이곳을 거쳐서 이동 처리 */
	// '장바구니'버튼에서는 '다시쇼핑하기' 처리 / '주문하기'버튼에서는 '주문창'으로 보내도록 처리했음
	@RequestMapping(value="/dbProductContent", method=RequestMethod.POST)
	public String dbProductContentPost(DbCartVO vo, HttpSession session, String flag) {
		String mid = (String) session.getAttribute("sMid");
		// DbCartVO(vo) : 사용자가 선택한 품목(기본품목+옵션)의 정보를 담고 있는 VO
		// DbCartVO(resVo) : 사용자가 기존에 장바구니에 담아놓은 적이 있는 품목(기본품목+옵션)의 정보를 담고 있는 VO
		
		// 장바구니 담을 시, 동일한 품목(제품)이라면 수량 누적 
		DbCartVO resVo = dbShopService.getDbCartProductOptionSearch(vo.getProductName(), vo.getOptionName(), mid);
		
		// 아래 if문은 나에게 맞게 설계해야 할 부분!
		if(resVo != null) { // 기존에 구매한 적이 있었다면, '현재구매내역' + '기존장바구니수량' 합쳐 update 시켜줘야 함
			// JS에서(script)는 동적배열(ex)dbProductContent.jsp) 선언이 가능하지만, java는 XXX
			String[] voOptionNums = vo.getOptionNum().split(",");
			String[] resOptionNums = resVo.getOptionNum().split(",");
			int[] nums = new int[99];
			String strNums = "";
			for(int i=0; i<voOptionNums.length; i++) {
				// strNums : 옵션index(0번옵션) + 옵션선택수량(2개) 합쳐 DB 저장 -> 0 + 2 = 2 
				nums[i] += (Integer.parseInt(voOptionNums[i]) + Integer.parseInt(resOptionNums[i]));
				strNums += nums[i];
				// 반복문 돌면서, 마지막 쉼표 제외 전까지 쉼표 추가
				if(i < nums.length - 1) strNums += ",";
			}
			vo.setOptionNum(strNums);
			dbShopService.setDbShopCartUpdate(vo);
		}
		else {
			// 처음 구매하는 제품이라면 장바구니에 insert
			dbShopService.setDbShopCartInput(vo);
		}
		
		if(flag.equals("order")) {
			return "redirect:/msg/cartOrderOk";
		}
		else {
			return "redirect:/msg/cartInputOk";
		}
	}
	
	/* 장바구니에 담겨있는 모든 상품들의 내역을 보여줌 - 주문 전단계(장바구니는 DB에 있는 자료를 바로 불러와서 처리하면 됨) */
	@RequestMapping(value="/dbCartList", method=RequestMethod.GET)
	public String dbCartGet(HttpSession session, DbCartVO vo, Model model) {
		String mid = (String) session.getAttribute("sMid");
		List<DbCartVO> vos = dbShopService.getDbCartList(mid);
		
		if(vos.size() == 0) {
			// 없다면 넘어가지 않도록 처리했지만, 혹시모르니 메세지 생성
			return "redirect:/msg/cartEmpty";
		}
		
		model.addAttribute("cartListVos", vos);
		return "dbShop/dbCartList";
	}
	
	/* 장바구니에서 품목 삭제버튼 처리 */
	@ResponseBody
	@RequestMapping(value="/dbCartDelete", method=RequestMethod.POST)
	public String dbCartDeleteGet(int idx) {
		dbShopService.dbCartDelete(idx);
		return "";
	}
	
	/* 카트에 담겨있는 품목들 중 주문한 품목들을 읽어와서 세션에 담아줌. 이때 고객의 정보도 함께 처리하며, 주문번호를 만들어 다음단계인 '결제'로 넘겨줌 */
	@RequestMapping(value="/dbCartList", method=RequestMethod.POST)
	public String dbCartListPost(HttpServletRequest request, Model model, HttpSession session,
			@RequestParam(name="baesong", defaultValue = "0", required = false) int baesong) {
		String mid = session.getAttribute("sMid").toString();
		
		// 주문한 상품의 '고유번호' 생성
		// 고유주문번호(idx) : 기존에 존재하는 주문테이블의 고유번호+1
		DbOrderVO maxIdx = dbShopService.getOrderMaxIdx();
		int idx = 1;
		if(maxIdx != null) idx = maxIdx.getMaxIdx() + 1;
		
		// 주문번호(날짜)
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String orderIdx = sdf.format(today) + idx;
		
		// 앞(jsp에서 checkItem)에서 선택된 것들(값이 1인 것)만 주문하겠다. (배열로 넘어옴)
		String[] idxChecked = request.getParameterValues("idxChecked");
		
		DbCartVO cartVo = new DbCartVO();
		List<DbOrderVO> orderVos = new ArrayList<DbOrderVO>();
		
		for(String strIdx : idxChecked) {
			// dbCart에 담긴 정보를 가져와서 orderVo로 모두 담는 작업
			// service에서 idx 변수명 변경!!!
			cartVo = dbShopService.getCartIdx(Integer.parseInt(strIdx));
			DbOrderVO orderVo = new DbOrderVO();
			orderVo.setProductIdx(cartVo.getProductIdx());
			orderVo.setProductName(cartVo.getProductName());
			orderVo.setMainPrice(cartVo.getMainPrice());
			orderVo.setThumbImg(cartVo.getThumbImg());
			orderVo.setOptionName(cartVo.getOptionName());
			orderVo.setOptionPrice(cartVo.getOptionPrice());
			orderVo.setOptionNum(cartVo.getOptionNum());
			orderVo.setTotalPrice(cartVo.getTotalPrice());
			orderVo.setCartIdx(cartVo.getIdx());
			orderVo.setBaesong(baesong);
			
			orderVo.setOrderIdx(orderIdx);
			orderVo.setMid(mid);
			
			orderVos.add(orderVo);
		}
		// model에 담으면 절대 안됨. 무조건 session에 담아야 함! (다음단계 넘어가면 정보들이 날아감)
		session.setAttribute("sOrderVos", orderVos);
		
		// 배송처리를 위해 현재 로그인된 사용자(고객)의 정보를 member2테이블에서 가져옴
		MemberVO memberVo = memberService.getMemberIdCheck(mid);
		model.addAttribute("memberVo", memberVo);
		
		return "dbShop/dbOrder";
	}
	
	/* 아임포트(KG이니시스) 결제 프로그램 사용 */
	@RequestMapping(value = "/sample", method = RequestMethod.POST)
	public String samplePost(PayMentVO vo) {
		return "dbShop/sample";
	}
	
	// 결제시스템(결제창 호출하기) - API이용
	@RequestMapping(value="/payment", method=RequestMethod.POST)
	public String paymentPost(DbOrderVO orderVo, PayMentVO payMentVo, DbBaesongVO baesongVo, HttpSession session, Model model) {
		model.addAttribute("payMentVo", payMentVo);
		
		session.setAttribute("sPayMentVo", payMentVo);
		session.setAttribute("sBaesongVo", baesongVo);
		
		return "dbShop/paymentOk";
	}
	
	/* 결제시스템 연습하기(결제창 호출 후 결제완료라면 처리하는 부분) */
	// 주문 완료후 주문내역을 '주문테이블(dbOrder)에 저장
	// 주문이 완료되었기에 주문된 물품은 장바구니(dbCartList)에서 내역을 삭제처리한다.
	// 사용한 세션은 제거시킨다.
	// 작업처리후 오늘 구매한 상품들의 정보(구매품목,결제내역,배송지)들을 model에 담아서 확인창으로 넘겨준다.
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/paymentResult", method=RequestMethod.GET)
	public String paymentResultGet(HttpSession session, PayMentVO receivePayMentVo, Model model) {
		// 주문내역 dbOrder/dbBaesong 테이블에 저장하기(앞에서 저장했던 세션에서 가져왔다.)
		List<DbOrderVO> orderVos = (List<DbOrderVO>) session.getAttribute("sOrderVos");
		PayMentVO payMentVo = (PayMentVO) session.getAttribute("sPayMentVo");
		DbBaesongVO baesongVo = (DbBaesongVO) session.getAttribute("sBaesongVo");
		
  	// 사용된 세션은 반환한다.
  	// session.removeAttribute("sOrderVos");  // 마지막 결재처리후에 결재결과창에서 확인하고 있기에 지우면 안됨
		session.removeAttribute("sBaesongVo");
		
		for(DbOrderVO vo : orderVos) {
			vo.setIdx(Integer.parseInt(vo.getOrderIdx().substring(8))); // 주문테이블에 고유번호를 셋팅한다.	
			vo.setOrderIdx(vo.getOrderIdx());        				// 주문번호를 주문테이블의 주문번호필드에 지정처리한다.
			vo.setMid(vo.getMid());							
			
			dbShopService.setDbOrder(vo);                 	// 주문내용을 주문테이블(dbOrder)에 저장.
			// service에서 idx로 변수명 변경!
			dbShopService.dbCartDeleteAll(vo.getCartIdx()); // 주문이 완료되었기에 장바구니(dbCart)테이블에서 주문한 내역을 삭체처리한다.
			
		}
		// 주문된 정보를 배송테이블에 담기위한 처리(기존 baesongVo에 담기지 않은 내역들을 담아주고 있다.)
		baesongVo.setOIdx(orderVos.get(0).getIdx());
		baesongVo.setOrderIdx(orderVos.get(0).getOrderIdx());
		baesongVo.setAddress(payMentVo.getBuyer_addr());
		baesongVo.setTel(payMentVo.getBuyer_tel());
		
		dbShopService.setDbBaesong(baesongVo);  // 배송내용을 배송테이블(dbBaesong)에 저장
		dbShopService.setMemberPointPlus((int)(baesongVo.getOrderTotalPrice() * 0.01), orderVos.get(0).getMid());	// 회원테이블에 포인트 적립하기(1%)
		
		payMentVo.setImp_uid(receivePayMentVo.getImp_uid());
		payMentVo.setMerchant_uid(receivePayMentVo.getMerchant_uid());
		payMentVo.setPaid_amount(receivePayMentVo.getPaid_amount());
		payMentVo.setApply_num(receivePayMentVo.getApply_num());
		
		// 오늘 주문에 들어간 정보들을 확인해주기위해 다시 session에 담아서 넘겨주고 있다.
		session.setAttribute("sPayMentVo", payMentVo);
		session.setAttribute("orderTotalPrice", baesongVo.getOrderTotalPrice());
		
		return "redirect:/msg/paymentResultOk";
	}
	
	//결재완료된 정보 보여주기
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value="/paymentResultOk", method=RequestMethod.GET)
	public String paymentResultOkGet(HttpSession session, Model model) {
		List<DbOrderVO> orderVos = (List<DbOrderVO>) session.getAttribute("sOrderVos");
		model.addAttribute("orderVos", orderVos);
		session.removeAttribute("sOrderVos");
		return "dbShop/paymentResult";
	}
	
	//현재 로그인 사용자가 주문내역 조회하기 폼 보여주기
	@RequestMapping(value="/dbMyOrder", method=RequestMethod.GET)
	public String dbMyOrderGet(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(name="pag", defaultValue="1", required=false) int pag,
			@RequestParam(name="pageSize", defaultValue="5", required=false) int pageSize) {
		String mid = (String) session.getAttribute("sMid");
		int level = (int) session.getAttribute("sLevel");
		if(level == 0) mid = "전체";
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "dbMyOrder", mid, "");
		
		// 오늘 구매한 내역을 초기화면에 보여준다.
		List<DbProductVO> vos = dbShopService.getMyOrderList(pageVo.getStartIdxNo(), pageSize, mid);
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo",pageVo);
		
		return "dbShop/dbMyOrder";
	}
	
	//날짜별 상태별 기존제품 구매한 주문내역 확인하기
	@RequestMapping(value="/myOrderStatus", method=RequestMethod.GET)
	public String myOrderStatusGet(
			HttpServletRequest request, 
			HttpSession session, 
			String startJumun, 
			String endJumun, 
			@RequestParam(name="pag", defaultValue="1", required=false) int pag,
			@RequestParam(name="pageSize", defaultValue="5", required=false) int pageSize,
 	  @RequestParam(name="conditionOrderStatus", defaultValue="전체", required=false) String conditionOrderStatus,
			Model model) {
		String mid = (String) session.getAttribute("sMid");
		// 레벨이 관리자라면, 전체를 보기 위해서 세션 불러옴
		int level = (int) session.getAttribute("sLevel");
		
		if(level == 0) mid = "전체";
		// 페이징처리 매개변수 자리 부족해서 searchString 변수 선언해서 나머지 넘겨줌
		String searchString = startJumun + "@" + endJumun + "@" + conditionOrderStatus;
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "myOrderStatus", mid, searchString);  // 4번째인자에 '아이디/조건'(을)를 넘겨서 part를 아이디로 검색처리하게 한다.
		
		List<DbBaesongVO> vos = dbShopService.getMyOrderStatus(pageVo.getStartIdxNo(), pageSize, mid, startJumun, endJumun, conditionOrderStatus);
		model.addAttribute("vos", vos);				
		model.addAttribute("startJumun", startJumun);
		model.addAttribute("endJumun", endJumun);
		model.addAttribute("conditionOrderStatus", conditionOrderStatus);
		model.addAttribute("pageVo", pageVo);
		
		return "dbShop/dbMyOrder";
	}

}
