package com.ask.sample.repository;

import com.ask.sample.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String>, AttachmentRepositoryCustom {
}
