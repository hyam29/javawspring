package com.spring.javawspring;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.service.ScheduleService;
import com.spring.javawspring.vo.ScheduleVO;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

	@Autowired
	ScheduleService scheduleService;
	
	
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public String scheduleGet() {
		scheduleService.getSchedule();
		return "schedule/schedule";
	}
	
	@RequestMapping(value = "/scheduleMenu", method = RequestMethod.GET)
	public String scheduleMenuGet(Model model, HttpSession session, String ymd) {
		String mid = (String) session.getAttribute("sMid");
		List<ScheduleVO> vos = scheduleService.getScheduleMenu(mid, ymd);
		
		model.addAttribute("vos", vos);
		model.addAttribute("scheduleCnt", vos.size());
		model.addAttribute("ymd", ymd);
		
		return "schedule/scheduleMenu";
	}
	
	/* 일정 등록 처리 */
	@ResponseBody
	@RequestMapping(value = "/scheduleInputOk", method = RequestMethod.POST)
	public String scheduleInputOkPost(ScheduleVO vo) {
		scheduleService.setScheduleInputOk(vo);
		return "";
	}
	
	/* 일정 삭제 처리 */
	@ResponseBody
	@RequestMapping(value = "/scheduleDelete", method = RequestMethod.POST)
	public String scheduleDeletePost(int idx) {
		int res = scheduleService.setScheduleDelete(idx);
		return res+"";
	}
	
	/* 일정 수정 처리 */
	@ResponseBody
	@RequestMapping(value = "/scheduleUpdateOk", method = RequestMethod.POST)
	public String scheduleUpdateOkPost(ScheduleVO vo, int idx) {
		int res = scheduleService.setScheduleUpdate(vo, idx);
		return res+"";
	}
	
	
	
}
