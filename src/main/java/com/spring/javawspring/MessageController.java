package com.spring.javawspring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {
	
	@RequestMapping(value="/msg/{msgFlag}", method=RequestMethod.GET)
	public String msgGet(@PathVariable String msgFlag, Model model, 
			// @RequestParam : null값 처리 및 초기값 지정(defaultValue) 가능
			@RequestParam(value="mid", defaultValue="", required=false) String mid,
			// pag, pageSize 넘기기 위해 flag 변수 생성
			@RequestParam(value="flag", defaultValue="", required=false) String flag) {
		
		if(msgFlag.equals("memberLoginOk")) {
			model.addAttribute("msg", mid + "님 로그인 되었습니다.");
			model.addAttribute("url", "member/memberMain");
		}
		else if(msgFlag.equals("memberLoginNo")) {
			model.addAttribute("msg", "아이디, 비밀번호를 확인하세요.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("memberLogout")) {
			model.addAttribute("msg", mid + "님 로그아웃 되었습니다. 안녕히가세요.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("guestInputOk")) {
			model.addAttribute("msg", "작성하신 방명록 글이 등록되었습니다!");
			model.addAttribute("url", "guest/guestList");
		}
		else if(msgFlag.equals("guestDeleteOk")) {
			model.addAttribute("msg", "해당 방명록 글이 삭제되었습니다.");
			model.addAttribute("url", "guest/guestList");
		}
		else if(msgFlag.equals("memberJoinOk")) {
			model.addAttribute("msg", "회원가입이 완료되었습니다! 로그인을 해주세요.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("memberIdCheckNo")) {
			model.addAttribute("msg", "중복된 아이디 입니다.");
			model.addAttribute("url", "member/memberJoin");
		}
		else if(msgFlag.equals("memberNickNameCheckNo")) {
			model.addAttribute("msg", "중복된 닉네임 입니다.");
			model.addAttribute("url", "member/memberJoin");
		}
		else if(msgFlag.equals("adminNo")) {
			model.addAttribute("msg", "당신은 관리자가 아닙니다.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("adminMemberDelOk")) {
			model.addAttribute("msg", "회원탈퇴 처리가 완료되었습니다.");
			model.addAttribute("url", "admin/member/adminMemberList");
		}
		else if(msgFlag.equals("memberNo")) {
			model.addAttribute("msg", "회원가입 후 우리사이트를 이용해주세요.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("levelCheckNo")) {
			model.addAttribute("msg", "회원님의 등급 확인이 필요한 페이지입니다.");
			model.addAttribute("url", "member/memberMain");
		}
		else if(msgFlag.equals("mailSendOk")) {
			model.addAttribute("msg", "메일이 전송되었습니다!");
			model.addAttribute("url", "study/mail/mailForm");
		}
		else if(msgFlag.equals("mamberImsiPwdOk")) {
			model.addAttribute("msg", "임시비밀번호를 메일로 발송하였습니다.\\n로그인 후 비밀번호를 변경해주세요.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("mamberImsiPwdNo")) {
			model.addAttribute("msg", "아이디, 이메일을 확인해주세요.");
			model.addAttribute("url", "member/memberPwdSearch");
		}
		else if(msgFlag.equals("memberPwdNo")) {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("url", "member/memberPwdUpdate");
		}
		else if(msgFlag.equals("memberDeletePwdNo")) {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("url", "member/memberDelete");
		}
		else if(msgFlag.equals("memberDeleteOk")) {
			model.addAttribute("msg", "회원탈퇴가 완료되었습니다.");
			model.addAttribute("url", "member/memberLogout");
		}
		else if(msgFlag.equals("memberPwdUpdateOk")) {
			model.addAttribute("msg", "비밀번호 변경이 완료되었습니다!");
			model.addAttribute("url", "member/memberMain");
		}
		else if(msgFlag.equals("memberUpdatePwdNo")) {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("url", "member/memberPwdCheck");
		}
		else if(msgFlag.equals("memberUpdatePwdOk")) {
			model.addAttribute("msg", "비밀번호가 일치합니다.\\n회원정보 수정 페이지로 이동합니다.");
			model.addAttribute("url", "member/memberUpdate");
		}
		else if(msgFlag.equals("memberUpdateOk")) {
			model.addAttribute("msg", "회원정보 수정이 완료되었습니다!");
			model.addAttribute("url", "member/memberMain");
		}
		else if(msgFlag.equals("memberUpdateNo")) {
			model.addAttribute("msg", "회원정보 수정에 실패하였습니다.");
			model.addAttribute("url", "member/memberUpdate");
		}
		else if(msgFlag.equals("fileUploadOk")) {
			model.addAttribute("msg", "파일이 업로드되었습니다!");
			model.addAttribute("url", "study/fileUpload/fileUploadForm");
		}
		else if(msgFlag.equals("fileUploadNo")) {
			model.addAttribute("msg", "파일 업로드에 실패하였습니다.");
			model.addAttribute("url", "study/fileUpload/fileUploadForm");
		}
		else if(msgFlag.equals("boardInputOk")) {
			model.addAttribute("msg", "작성하신 글이 성공적으로 등록되었습니다!");
			model.addAttribute("url", "board/boardList");
		}
		else if(msgFlag.equals("boardInputNo")) {
			model.addAttribute("msg", "작성글 등록에 실패하였습니다.");
			// boardInput으로 보낼 때, pag와 pageSize 넘길거면 컨트롤러에서도 넘겼어야 함...! -> 삭제에서 pag,pageSize 넘김! 바로 아래 삭제참고!
			model.addAttribute("url", "board/boardInput");
		}
		else if(msgFlag.equals("boardDeleteOk")) {
			model.addAttribute("msg", "게시글이 삭제되었습니다.");
			// flag로 pag, pageSize 넘겨줌
			model.addAttribute("url", "board/boardList"+flag);
		}
		else if(msgFlag.equals("boardUpdateOk")) {
			model.addAttribute("msg", "게시글이 수정되었습니다.");
			model.addAttribute("url", "board/boardList"+flag);
		}
		else if(msgFlag.equals("pdsInputOk")) {
			model.addAttribute("msg", "자료실에 파일이 업로드 되었습니다.");
			model.addAttribute("url", "pds/pdsList");
		}
		else if(msgFlag.equals("wmMemberIdNo")) {
			model.addAttribute("msg", "입력하신 아이디는 없는 회원입니다.");
			model.addAttribute("url", "webMessage/webMessage?mSw=0");
		}
		else if(msgFlag.equals("wmInputOk")) {
			model.addAttribute("msg", "메세지가 전송되었습니다.");
			model.addAttribute("url", "webMessage/webMessage?mSw=1");
		}
		else if(msgFlag.equals("wmDeleteAll")) {
			model.addAttribute("msg", "휴지통을 모두 비웠습니다.");
			model.addAttribute("url", "webMessage/webMessage?mSw=1");
		}
		else if(msgFlag.equals("dbProductInputOk")) {
			model.addAttribute("msg", "상품이 등록되었습니다.");
			model.addAttribute("url", "dbShop/dbShopList");
		}
		
		
		return "include/message";
	}
}
