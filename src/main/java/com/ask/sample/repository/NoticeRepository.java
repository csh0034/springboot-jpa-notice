package com.ask.sample.repository;

import com.ask.sample.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, String>, NoticeRepositoryCustom {
}