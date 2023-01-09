package com.spring.javawspring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.GuestService;
import com.spring.javawspring.vo.GuestVO;

@Controller
@RequestMapping("/guest")
public class GuestController {
	
	@Autowired
	GuestService guestService;
	
	@Autowired
	PageProcess pageProcess;
	
	/*
	@RequestMapping(value="/guestList", method=RequestMethod.GET)
	public String guestListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {
		// 1. 페이지(pag)를 결정 = 1
		// int pageSize = 3; // 2. 한 페이지의 분량 결정 = 3
		int totRecCnt = guestService.totRecCnt(); // 3. 총 레코드 건수를 구함
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1; // 4. 전체 페이지 건수를 구함
		int startIdxNo = (pag - 1) * pageSize; // 5. 현재 페이지의 시작 index 번호를 구함
		int curScrStartNo = totRecCnt - startIdxNo; // 6. 현재 화면에 보여주는 시작번호를 구함
		
		// 블록 페이징 처리 (3단계) => 블록의 시작번호를 0번부터 처리
		
		int blockSize = 3; // 1. 블록의 크기를 결정 => '3' 
		int curBlock = (pag - 1) / blockSize;// 2. 현재 페이지가 위치하고 있는 블록 번호를 구함 (ex) 1페이지 = 0블록, 3페이지 = 0블록, 5페이지 = 1블록)
		int lastBlock = (totPage - 1) / blockSize; // 3. 마지막 블록 구함
		
		ArrayList<GuestVO> vos = guestService.getGuestList(startIdxNo, pageSize);
		
		model.addAttribute("vos", vos);// 이렇게 담으면 첫 페이지만 나옴
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		model.addAttribute("pageSize", pageSize);
		
		return "guest/guestList";
	}
	*/
	
	/* Pagination 이용한 list 처리 */
	@RequestMapping(value="/guestList", method=RequestMethod.GET)
	public String guestListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {

		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "guest", "", "");
		
		List<GuestVO> vos = guestService.getGuestList(pageVo.getStartIdxNo(), pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		return "guest/guestList";
	}
	
	@RequestMapping(value="/guestInput", method=RequestMethod.GET)
	public String guestInputGet() {
		return "guest/guestInput";
	}
	
	@RequestMapping(value="/guestInput", method=RequestMethod.POST)
	public String guestInputPost(GuestVO vo) {
		guestService.setGuestInput(vo);
		
		return "redirect:/msg/guestInputOk";
	}
	
	@RequestMapping(value="/guestDelete", method=RequestMethod.GET)
	public String guestDeleteGet(int idx) {
		guestService.setGuestDelete(idx);
		
		return "redirect:/msg/guestDeleteOk";
	}
	
}
