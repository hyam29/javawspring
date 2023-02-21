package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.vo.ChartVO;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.QrCodeVO;
import com.spring.javawspring.vo.TransactionVO;

public interface StudyService {

	public String[] getCityStringArr(String dodo);

	public ArrayList<String> getCityArrayListArr(String dodo);

	public GuestVO getGuestMid(String mid);

	public ArrayList<GuestVO> getGuestNames(String mid);

	public ArrayList<GuestVO> getGuestPart(String part, String mid);

	public int fileUpload(MultipartFile fName);

	public void getCalendar();

	public String qrCreate(String mid, String moveFlag, String realPath);

	public void setMovieReservation(QrCodeVO vo);

	public QrCodeVO getMovieReservation(String idxSearch);

	public String qrCreate2(String moveFlag, String realPath);

	public QrCodeVO qrCodeSearch(String idx);

	public KakaoAddressVO getKakaoAddressName(String address);

	public void setKakaoAddressName(KakaoAddressVO vo);

	public List<KakaoAddressVO> getAddressNameList();

	public void setKakaoAddressDelete(String address);

	public List<KakaoAddressVO> getDistanceList();

	public void setTransInput1(TransactionVO vo);

	public void setTransInput2(TransactionVO vo);

	public List<TransactionVO> setTransList();

	public void setTransInput(TransactionVO vo);

	public int thumbnailCreate(MultipartFile file);

	public List<ChartVO> getRecentlyVisitCount(int i);
	
}
