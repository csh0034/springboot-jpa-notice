package com.ask.sample.repository;

import com.ask.sample.vo.response.NoticeResponseVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

  Page<NoticeResponseVO> findNotices(String title, Pageable pageable);
}