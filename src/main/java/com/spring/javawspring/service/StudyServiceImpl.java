package com.spring.javawspring.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spring.javawspring.common.DistanceCal;
import com.spring.javawspring.dao.StudyDAO;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.QrCodeVO;
import com.spring.javawspring.vo.TransactionVO;

@Service
public class StudyServiceImpl implements StudyService {
	@Autowired
	StudyDAO studyDAO;
	
	@Autowired
	JavaMailSender mailSender;

	@Override
	public String[] getCityStringArr(String dodo) {
		String[] strArr = new String[100];
		
		if(dodo.equals("서울")) {
			strArr[0] = "강남구";
			strArr[1] = "서초구";
			strArr[2] = "동대문구";
			strArr[3] = "성북구";
			strArr[4] = "마포구";
			strArr[5] = "강동구";
			strArr[6] = "관악구";
			strArr[7] = "광진구";
			strArr[8] = "중구";
			strArr[9] = "서구";
		}
		else if(dodo.equals("경기")) {
			strArr[0] = "수원시";
			strArr[1] = "이천시";
			strArr[2] = "일산시";
			strArr[3] = "용인시";
			strArr[4] = "시흥시";
			strArr[5] = "광명시";
			strArr[6] = "광주시";
			strArr[7] = "의정부시";
			strArr[8] = "평택시";
			strArr[9] = "안성시";
		}
		else if(dodo.equals("충북")) {
			strArr[0] = "청주시";
			strArr[1] = "충주시";
			strArr[2] = "제천시";
			strArr[3] = "괴산군";
			strArr[4] = "진천군";
			strArr[5] = "음성군";
			strArr[6] = "옥천군";
			strArr[7] = "영동군";
			strArr[8] = "단양군";
			strArr[9] = "증평군";
		}
		else if(dodo.equals("충남")) {
			strArr[0] = "천안시";
			strArr[1] = "병천시";
			strArr[2] = "아산시";
			strArr[3] = "공주시";
			strArr[4] = "계룡시";
			strArr[5] = "논산시";
			strArr[6] = "당진군";
			strArr[7] = "보령시";
			strArr[8] = "옥산군";
			strArr[9] = "예산군";
		}
		
		
		return strArr;
	}

	@Override
	public ArrayList<String> getCityArrayListArr(String dodo) {
		ArrayList<String> vos = new ArrayList<String>();
		
		if(dodo.equals("서울")) {
			vos.add("강남구"); // strArr[] -> vos.add() 로 변경되어야 함.
			vos.add("서초구");
			vos.add("동대문구");
			vos.add("성북구");
			vos.add("마포구");
			vos.add("강동구");
			vos.add("관악구");
			vos.add("광진구");
			vos.add("중구");
			vos.add("서구");
		}
		else if(dodo.equals("경기")) {
			vos.add("수원시");
			vos.add("이천시");
			vos.add("일산시");
			vos.add("용인시");
			vos.add("시흥시");
			vos.add("광명시");
			vos.add("광주시");
			vos.add("의정부시");
			vos.add("평택시");
			vos.add("안성시");
		}
		else if(dodo.equals("충북")) {
			vos.add("청주시");
			vos.add("충주시");
			vos.add("제천시");
			vos.add("괴산군");
			vos.add("진천군");
			vos.add("음성군");
			vos.add("옥천군");
			vos.add("영동군");
			vos.add("단양군");
			vos.add("증평군");
		}
		else if(dodo.equals("충남")) {
			vos.add("천안시");
			vos.add("병천시");
			vos.add("아산시");
			vos.add("공주시");
			vos.add("계룡시");
			vos.add("논산시");
			vos.add("당진군");
			vos.add("보령시");
			vos.add("옥산군");
			vos.add("예산군");
		}
		
		return vos;
	}

	@Override
	public GuestVO getGuestMid(String mid) {
		// 1. guestDAO가 없어서 위에 @Autowired 걸어서 또 생성해두면 됨.
		// 2. studyDAO. 로 guest DB 생성 또 해주는 개념으로 하면 됨. 어차피 DAO는 직접 일하는 곳 아니니까! (Mapper 경로만 변경해주면 됨)
		return studyDAO.getGuestMid(mid);
	}

	@Override
	public ArrayList<GuestVO> getGuestNames(String mid) {
		return studyDAO.getGuestNames(mid);
	}

	@Override
	public ArrayList<GuestVO> getGuestPart(String part, String mid) {
		return studyDAO.getGuestPart(part, mid);
	}

	// fileUpload와 writeFile 메소드 2개 common package에 넣어두기 (많이 사용되므로) -> fileUpload와 writeFile 세트
	@Override
	public int fileUpload(MultipartFile fName) {
		int res = 0;
		try {
			UUID uid = UUID.randomUUID();
			String oFileName = fName.getOriginalFilename();
			String saveFileName = uid + "_" +oFileName;
			
			writeFile(fName, saveFileName);
			res = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
		
	}

	public void writeFile(MultipartFile fName, String saveFileName) throws IOException {
		byte[] data = fName.getBytes();
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		// request.getRealPath("/resources/pds/temp/");
		// 노란줄이 싫다면 아래와 같이 작성. 
		String realPath = request.getSession().getServletContext().getRealPath("/resources/pds/temp/");
		// 업로드 된 파일을 꺼내와서 서버에 저장하는 부분
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName);
		fos.write(data);
		fos.close();
	}

	/* 달력 처리 */
	@Override
	public void getCalendar() {
		// model객체를 사용하게되면 불필요한 메소드가 많이 따라오기에 여기서는 request객체를 사용했다.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 오늘 날짜 저장시켜둔다.(calToday변수, 년(toYear), 월(toMonth), 일(toDay))
		Calendar calToday = Calendar.getInstance();
		int toYear = calToday.get(Calendar.YEAR);
		int toMonth = calToday.get(Calendar.MONTH);
		int toDay = calToday.get(Calendar.DATE);
				
		// 화면에 보여줄 해당 '년(yy)/월(mm)'을 셋팅하는 부분(처음에는 오늘 년도와 월을 가져오지만, '이전/다음'버튼 클릭하면 해당 년과 월을 가져오도록 한다.
		Calendar calView = Calendar.getInstance();
		int yy = request.getParameter("yy")==null ? calView.get(Calendar.YEAR) : Integer.parseInt(request.getParameter("yy"));
	  int mm = request.getParameter("mm")==null ? calView.get(Calendar.MONTH) : Integer.parseInt(request.getParameter("mm"));
	  
	  if(mm < 0) { // 1월에서 전월 버튼을 클릭시에 실행
	  	yy--;
	  	mm = 11; // 월은 0이 1월 (12월이 11)
	  }
	  if(mm > 11) { // 12월에서 다음월 버튼을 클릭시에 실행
	  	yy++;
	  	mm = 0;
	  }
	  calView.set(yy, mm, 1);		// 현재 '년/월'의 1일을 달력의 날짜로 셋팅한다.
	  
	  int startWeek = calView.get(Calendar.DAY_OF_WEEK);  						// 해당 '년/월'의 1일에 해당하는 요일값을 숫자로 가져온다.
	  int lastDay = calView.getActualMaximum(Calendar.DAY_OF_MONTH);  // 해당월의 마지막일자(getActualMaxximum메소드사용)를 구한다.
	  
	  // 화면에 보여줄 년월기준 전년도/다음년도를 구하기 위한 부분
	  int prevYear = yy;  			// 전년도
	  int prevMonth = (mm) - 1; // 이전월
	  int nextYear = yy;  			// 다음년도
	  int nextMonth = (mm) + 1; // 다음월
	  
	  if(prevMonth == -1) {  // 1월에서 전월 버튼을 클릭시에 실행..
	  	prevYear--;
	  	prevMonth = 11;
	  }
	  
	  if(nextMonth == 12) {  // 12월에서 다음월 버튼을 클릭시에 실행..
	  	nextYear++;
	  	nextMonth = 0;
	  }
	  
	  // 현재달력에서 앞쪽의 빈공간은 '이전달력'을 보여주고, 뒷쪽의 남은공간은 '다음달력'을 보여주기위한 처리부분(아래 6줄)
	  Calendar calPre = Calendar.getInstance(); // 이전달력
	  calPre.set(prevYear, prevMonth, 1);  			// 이전 달력 셋팅
	  int preLastDay = calPre.getActualMaximum(Calendar.DAY_OF_MONTH);  // 해당월의 마지막일자를 구한다.
	  
	  Calendar calNext = Calendar.getInstance();// 다음달력
	  calNext.set(nextYear, nextMonth, 1);  		// 다음 달력 셋팅
	  int nextStartWeek = calNext.get(Calendar.DAY_OF_WEEK);  // 다음달의 1일에 해당하는 요일값을 가져온다.
	  
	  /* ---------  아래는  앞에서 처리된 값들을 모두 request객체에 담는다.  -----------------  */
	  
	  // 오늘기준 달력...
	  request.setAttribute("toYear", toYear);
	  request.setAttribute("toMonth", toMonth);
	  request.setAttribute("toDay", toDay);
	  
	  // 화면에 보여줄 해당 달력...
	  request.setAttribute("yy", yy);
	  request.setAttribute("mm", mm);
	  request.setAttribute("startWeek", startWeek);
	  request.setAttribute("lastDay", lastDay);
	  
	  // 화면에 보여줄 해당 달력 기준의 전년도, 전월, 다음년도, 다음월 ...
	  request.setAttribute("preYear", prevYear);
		request.setAttribute("preMonth", prevMonth);
		request.setAttribute("nextYear", nextYear);
		request.setAttribute("nextMonth", nextMonth);
		
		// 현재 달력의 '앞/뒤' 빈공간을 채울, 이전달의 뒷부분과 다음달의 앞부분을 보여주기위해 넘겨주는 변수
		request.setAttribute("preLastDay", preLastDay);				// 이전달의 마지막일자를 기억하고 있는 변수
		request.setAttribute("nextStartWeek", nextStartWeek);	// 다음달의 1일에 해당하는 요일을 기억하고있는 변수
	}

	/* QR 코드 생성 처리 */
	@Override
	public String qrCreate(String mid, String moveFlag, String realPath) {
		String qrCodeName = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		UUID uid = UUID.randomUUID();
		// String strUid = uid.toString().substring(0,2);
		String strUid = uid.toString().substring(0,8);
		
		qrCodeName = sdf.format(new Date()) + "_" + mid + "_" + moveFlag + "_" + strUid;
		
		try {
			// 껍데기 생성(폴더)
			File file = new File(realPath);
			// exists() : 있어? 없어? -> mkdirs() : 없으면 만들어줘! (makeDirectorys) (mkdirs 아래아래 계속 만드는 것)
			if(!file.exists()) file.mkdirs();
			
			String codeFlag = new String(moveFlag.getBytes("UTF-8"), "ISO-8859-1") + "_" +strUid;
			
			/* QR코드 생성 */
			
			// QR코드 색상 정하기 (전경색(글자색), 배경색)
			// 0x : 16진수로 작성하겠다. (8진수는 0만 작성)
			// FF : 이 뒤 문자인식
			// 000000 : 검정색, FFFFFF : 흰색
			int qrCodeColor = 0xFF000000;
			int qrCodeBackColor = 0xFFFFFFFF;
			
			// QR코드 객체 생성 (google의 zxing 라이브러리 없으면 생성 안됨)
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			//
			// BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeName 써도 됨, null로 해도 되지만 더 다양한 표현을 위해 null 사용X, 세로길이, 가로길이);
			// BitMatrix bitMatrix = qrCodeWriter.encode(codeFlag, BarcodeFormat.QR_CODE, qrCodeColor, qrCodeBackColor);
			BitMatrix bitMatrix = qrCodeWriter.encode(codeFlag, BarcodeFormat.QR_CODE, 200, 200);
			
			MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(qrCodeColor, qrCodeBackColor);
			BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix,matrixToImageConfig);
			
			ImageIO.write(bufferedImage, "png", new File(realPath + qrCodeName + ".png"));
			
			
			// 예외처리 두개 두지 말고 그냥 Exception으로 하나로 합쳐도 됨
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
		/* qr 코드 메일 전송 처리 */
		try {
			String title = "예약사항을 확인해주세요.";
			String toMail = moveFlag;
			String content = "";
			
			// mailSender 위에 @Autowired 어노테이션 해둬야 함!
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			
			// 메일보관함에 회원이 보내온 메세지들을 모두 저장시킴
			messageHelper.setTo(toMail); // 위에 변수 선언안하고 여기에 vo.getToMail()로 작성해도 됨.
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			// 전송 시, 내용(content)이 없더라도 그냥 보내짐. 따라서, 정보를 좀 더 추가해서 보내는 것이 좋음!
			
			// 메세지 보관함의 내용(content) 편집 후 필요한 정보를 좀 더 추가해서 전송 처리
			// project때는, 방문하기 주소를 memberLogin으로 보내면 됨!
			content += "<br><hr><h3>예약번호는 <font coloer='blue'><b>"+strUid+"</b></font>이며, 발행된 qr코드는 다음과 같습니다. <br/>";
			content += "<p><img src=\"cid:qrCodeName.png\" width='300px' /></p>";
			content += "<p>방문하기 ▶ <a href='http://49.142.157.251:9090/green2209J_04/'>그린현대미술관</a></p>";
			// 경로 작은따옴표 안먹을 때 있어서, ""로 작성
			content += "<p><img src=\"cid:main.png\" width='500px' ></p>";
			content += "<br><hr><h3><font color='grey'>from. Green Museum<font><h3><hr><br>";
			// 편집된 content로 보내겠다.
			messageHelper.setText(content, true);

			// 기재된 그림파일의 경로를 따로 표시처리 한 후 보관함에 다시 저장 (순서 중요)
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			FileSystemResource file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/data/qrCode/"+qrCodeName+".png"));
			// 그림파일 저장("그림파일명", 객체변수(file))
			messageHelper.addInline("qrCodeName.png", file);
			
			// 메일 전송하기
			mailSender.send(message);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return qrCodeName;
	}

		

	@Override
	public void setMovieReservation(QrCodeVO vo) {
		studyDAO.setMovieReservation(vo);
	}

	@Override
	public QrCodeVO getMovieReservation(String idxSearch) {
		return studyDAO.getMovieReservation(idxSearch);
	}

	/* 선생님꺼 QR코드 */
	@Override
	public String qrCreate2(String bigo, String realPath) {
		String qrCodeName = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		UUID uid = UUID.randomUUID();
		String idx = uid.toString().substring(0,8);				// 고유번호로 지정한다.
		qrCodeName = sdf.format(new Date()) + "_" + idx;	// 저장될 파일명
		
		try {
			File file = new File(realPath);
			if(!file.exists()) file.mkdirs();
			String moveFlag = new String(bigo.getBytes("UTF-8"), "ISO-8859-1");	// qr코드 생성시에 moveFlag의 내용으로 qr코드가 만들어진다.(DB에 저장시 한글이 깨지기에 전송받은 bigo변수값은 변경시키지 않기위해 새로운 변수로 받았다.)
			
			// qr코드 만들기
			int qrCodeColor = 0xFF000000;			// qr코드 전경색(글자색)
			int qrCodeBackColor = 0xFFFFFFFF;	// qr코드 배경색
			
			QRCodeWriter qrCodeWriter = new QRCodeWriter();	// QR코드 객체 생성(moveFlag에 저장된 정보로 qr코드를 생성한다.)
			BitMatrix bitMatrix = qrCodeWriter.encode(moveFlag, BarcodeFormat.QR_CODE, 200, 200);		// QR코드저장시 크기(폭/높이) 지정
			
			MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(qrCodeColor, qrCodeBackColor);
			BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
			
			ImageIO.write(bufferedImage, "png", new File(realPath + qrCodeName + ".png"));
			
			// 생성된 QR코드의 정보를 DB에 저장한다.
			studyDAO.setQrCreateDB(idx, qrCodeName+".png", bigo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
		return qrCodeName;
	}

	@Override
	public QrCodeVO qrCodeSearch(String idx) {
		return studyDAO.qrCodeSearch(idx);
	}

	@Override
	public KakaoAddressVO getKakaoAddressName(String address) {
		return studyDAO.getKakaoAddressName(address);
	}

	@Override
	public void setKakaoAddressName(KakaoAddressVO vo) {
		studyDAO.setKakaoAddressName(vo);
	}

	@Override
	public List<KakaoAddressVO> getAddressNameList() {
		return studyDAO.getAddressNameList();
	}

	@Override
	public void setKakaoAddressDelete(String address) {
		studyDAO.setKakaoAddressDelete(address);
	}

	/* 거리 계산 처리 */
	@Override
	public List<KakaoAddressVO> getDistanceList() {
		double centerLat = 36.63514004150315;
		double centerLongi = 127.45952978085465;
		
		// DB에서 LIST 가져오기
		List<KakaoAddressVO> dbVOS = studyDAO.getkakaoList();
		
		// 계산 완료한 값들을 리턴타입으로 돌려보내기 위한 list 생성
		ArrayList<KakaoAddressVO> vos = new ArrayList<KakaoAddressVO>();
		
		// DistanceCal distanceCal = new DistanceCal();
		
		for(int i=0; i<dbVOS.size(); i++) {
			double distance = DistanceCal.distance(centerLat, centerLongi, dbVOS.get(i).getLatitude(), dbVOS.get(i).getLongitude(), "kilometer");
			if(distance < 15) {
				vos.add(dbVOS.get(i));
			}
		}
		
		return vos;
	}

	@Override
	public void setTransInput1(TransactionVO vo) {
		studyDAO.setTransInput1(vo);
	}

	@Override
	public void setTransInput2(TransactionVO vo) {
		studyDAO.setTransInput2(vo);
		
	}

	@Override
	public List<TransactionVO> setTransList() {
		return studyDAO.setTransList();
	}

	/* 많이 사용되는 트랜잭션 위치 */
	@Transactional
	@Override
	public void setTransInput(TransactionVO vo) {
		studyDAO.setTransInput(vo);
		
	}
	
	
}
