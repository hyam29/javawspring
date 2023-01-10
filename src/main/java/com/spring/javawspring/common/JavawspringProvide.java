package com.spring.javawspring.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

public class JavawspringProvide {
	public int fileUpload(MultipartFile fName) {
		int res = 0;
		try {
			UUID uid = UUID.randomUUID();
			String oFileName = fName.getOriginalFilename();
			String saveFileName = uid + "_" +oFileName;
			
			writeFile(fName, saveFileName, "");
			res = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
		
	}
	
	public void writeFile(MultipartFile fName, String saveFileName, String flag) throws IOException {
		byte[] data = fName.getBytes();
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// request.getRealPath("/resources/~~~");
		// 노란줄이 싫다면 아래와 같이 작성. 
		String realPath = request.getSession().getServletContext().getRealPath("/resources/"+flag+"/");
					
		// 업로드 된 파일을 꺼내와서 서버에 저장하는 부분
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName);
		fos.write(data);
		fos.close();
	}
}
