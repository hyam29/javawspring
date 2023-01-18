package com.spring.javawspring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.PdsService;
import com.spring.javawspring.vo.PdsVO;

@Controller
@RequestMapping("/pds")
public class PdsController {

	@Autowired
	PdsService pdsService;
	
	@Autowired
	PageProcess pageProcess;
	
	
	@RequestMapping(value = "/pdsList", method=RequestMethod.GET)
	public String adminMainGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "pds", part, "");
		List<PdsVO> vos = pdsService.getPdsList(pageVo.getStartIdxNo(), pageVo.getPageSize(), part);
		pageVo.setPart(part);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		return "pds/pdsList";
	}
	
	/* PDS 등록 처리 폼 */
	@RequestMapping(value = "/pdsInput", method = RequestMethod.GET)
	public String pdsInputGet() {
		return "pds/pdsInput";
	}
	
	/* PDS 등록 처리 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/pdsInput", method = RequestMethod.POST)
	public String pdsInputPost(PdsVO vo,
			MultipartHttpServletRequest file) {
		String pwd = vo.getPwd();
		SecurityUtil security = new SecurityUtil(); // static으로 생성해 두었기 때문에, 따로 생성 XXX (호출하면 됨)
		pwd = security.encryptSHA256(pwd);
		vo.setPwd(pwd);
		
		// 멀티파일을 서버에 저장시키고, 파일의 정보를 vo에 담아서 DB에 저장시킨다. 
		pdsService.setPdsInput(file, vo);
		
		return "redirect:/msg/pdsInputOk";
	}

	// 파일 다운로드시 다운횟수 증가하기
	@ResponseBody
	@RequestMapping(value = "/pdsDownNum", method = RequestMethod.POST)
	public String pdsInputPost(int idx) {
		pdsService.setPdsDownNum(idx);
		return "pds/pdsInput";
	}
	
	/* PDS 내용 삭제 처리 */
	@ResponseBody
	@RequestMapping(value = "/pdsDelete", method = RequestMethod.POST)
	public String pdsDeletePost(int idx, String pwd) {
		// SecurityUtil security = new SecurityUtil(); // SecurityUtil을 static으로 선언하였으므로 메소드영역에 올라와 있음! 이때는 클래스명으로 호출해서 사용! (따로 생성XXX -> 생성하여 사용하면 비효율적) 
		pwd = SecurityUtil.encryptSHA256(pwd);
		
		// 삭제처리 전 비밀번호를 먼저 체크하여 맞아야 삭제 처리
		PdsVO vo = pdsService.getPdsContent(idx);
		if(!pwd.equals(vo.getPwd())) return "0";
		
		// 비밀번호가 일치 -> 파일 삭제 후 DB 내역 삭제 처리 (idx가 아닌 vo넘겨야 됨(fSName삭제 해야하므로))
		pdsService.setPdsDelete(vo);
		
		return "1";
	}
	
	/* PDS 전체 다운로드 처리 */
	@RequestMapping(value="/pdsTotalDown", method = RequestMethod.GET)
	public String pdsTotalDownGet(HttpServletRequest request, int idx) throws IOException {
		// 파일 압축다운로드 전 다운로드 수 증가
		pdsService.setPdsDownNum(idx);
		
		// 파일이 여러개일 경우, 모든 파일을 하나의 파일로 압축(=통합)하여 다운로드 (이때, 압축파일명은 '글제목'으로 처리)
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/pds/");
		
		PdsVO vo = pdsService.getPdsContent(idx);
		
		String[] fNames = vo.getFName().split("/");
		String[] fSNames = vo.getFSName().split("/");
		
		// 여러 파일을 .zip 파일로 합쳐지기 위한 임시 저장 경로
		String zipPath = realPath + "temp/";
		// .zip 파일명과 확장자 결정
		String zipName = vo.getTitle() + ".zip";
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		// temp에서 합쳐진 파일들을 zip으로 다시 output해야햐므로 zipOutputStream 생성 (객체 껍데기 만들어서 생성해야 함)
		// 오버라이드가 아니므로, 예외처리를 throw하면 됨 (서비스 객체(serviceImple)에서 처리할 시, 오버라이드되어 있어 throw가 안되므로 직접 예외처리 해야 함) 
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath + zipName));
		
		byte[] buffer = new byte[2048];
		
		// fNames 배열 길이만큼 반복해도 됨! (i<fNames.length)
		for(int i=0; i<fSNames.length; i++ ) {
			// 서버에 올리는 이름 -> fSNames
			fis = new FileInputStream(realPath + fSNames[i]);
			// temp에는 원래 올렸던 이름이 와야 하므로, fNames 
			fos = new FileOutputStream(zipPath + fNames[i]);
			
			// 파일을 zipPath 경로에 옮겨서 이름 변경하여 저장
			File moveAndRename = new File(zipPath + fNames[i]);
			
			// fos에 파일 쓰기 작업 처리
			int data = 0;
			while((data = fis.read(buffer, 0, buffer.length)) != -1) {
				fos.write(buffer, 0, data);
			}
			fos.flush();
			fos.close();
			fis.close();
			
			// zip파일에 fos를 넣는 작업 처리 (fis 다시 생성해야 함. moveAndRename된 파일들 넣으려고) => temp폴더 안에서 하나의 글제목.zip 으로 파일들 넣는 작업!
			fis = new FileInputStream(moveAndRename);
			// zip파일에 파일들 넣기위한 zip껍데기 생성
			zout.putNextEntry(new ZipEntry(fNames[i]));
			
			while((data = fis.read(buffer, 0, buffer.length)) != -1) {
				zout.write(buffer, 0, data);
			}
			zout.flush();
			zout.closeEntry();
			fis.close();
		}
		// zout.close는 반복문을 마친 후 닫아줘야 함! 위까지는 하나의 .zip에 파일들 쭉쭉 집어넣는 작업 처리 중인 것
		zout.close();
		// url로 zipName 넘길 때, 한글처리(인코딩) 무조건 해야 함! (java.net.URLEncoder.encode(파일명))
		return "redirect:/pds/pdsDownAction?file="+java.net.URLEncoder.encode(zipName);
	}
	
	// 파일 전체다운로드 다운받는 처리 (서버->클라이언트)
	@RequestMapping(value="/pdsDownAction", method=RequestMethod.GET)
	public void pdsDownActionGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String file = request.getParameter("file"); // 클래스에서 매개변수로 받아도 되고, getParameter로 받아도 됨
		String downPathFile = request.getSession().getServletContext().getRealPath("resources/data/pds/temp/")+file;
		File downFile = new File(downPathFile);
		// IOExceiption 으로 예외처리 throws (아래의 두줄은 예약어!!!)
		String downFileName = new String(file.getBytes("UTF-8"), "8859_1");
		response.setHeader("Content-Disposition", "attachment;filename="+downFileName);
		
		FileInputStream fis = new FileInputStream(downFile);
		// 클라이언트에게로! (servlet은 무조건 response로 호출)
		ServletOutputStream sos = response.getOutputStream(); 
		byte[] buffer = new byte[2048];
		int data = 0;
		while((data = fis.read(buffer, 0, buffer.length)) != -1) {
			sos.write(buffer, 0, data);
		}
		sos.flush();
		sos.close();
		fis.close();
		
		// 다운로드 완료 후 temp폴더의 파일들 모두 삭제 처리
		// 이미지 위에서 downFile 로 File 객체를 생성해뒀으므로, 바로 삭제하면 됨. (또 생성 안해도 됨)
		/* new File(downPathFile).delete(); */
		downFile.delete();
		// 이후 temp안에 있는 원본 파일들도 지워야 함. 하지만, 관리자 통계를 위해서 관리자메뉴에서 삭제 처리
	}
	
	
}
