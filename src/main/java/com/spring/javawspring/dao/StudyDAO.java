package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.QrCodeVO;

public interface StudyDAO {

	GuestVO getGuestMid(@Param("name") String mid);

	ArrayList<GuestVO> getGuestNames(@Param("name") String mid);

	ArrayList<GuestVO> getGuestPart(@Param("part") String part, @Param("name") String mid);

	void setMovieReservation(@Param("vo") QrCodeVO vo);

	QrCodeVO getMovieReservation(@Param("idxSearch") String idxSearch);

	public void setQrCreateDB(@Param("idx") String idx, @Param("qrCode") String qrCode, @Param("bigo") String bigo);

	public QrCodeVO qrCodeSearch(@Param("idx") String idx);

	KakaoAddressVO getKakaoAddressName(@Param("address") String address);

	void setKakaoAddressName(@Param("vo") KakaoAddressVO vo);

	List<KakaoAddressVO> getAddressNameList();

	void setKakaoAddressDelete(@Param("address") String address);

	List<KakaoAddressVO> getkakaoList();

}
