package com.spring.javawspring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.WebMessageVO;

public interface WebMessageDAO {

	public WebMessageVO getWmMessageOne(@Param("idx") int idx, @Param("mid") String mid, @Param("mFlag") int mFlag);

	public int totRecCnt(@Param("mid") String mid, @Param("mSw") int mSw);

	public List<WebMessageVO> getWmMessageList(@Param("mid") String mid, @Param("mSw") int mSw, @Param("startIdxNo") int startIdxNo, @Param("pageSize") int pageSize);

	public void setWmInputOk(@Param("vo") WebMessageVO vo);

	public void setWmUpdate(@Param("idx") int idx, @Param("mid") String mid);

	public void setWmDeleteCheck(@Param("idx") int idx, @Param("mFlag") int mFlag);

	public void setWmDeleteAll(@Param("mid") String mid);
	
}
