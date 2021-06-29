package com.ask.sample.repository;

import com.ask.sample.domain.Attachment;
import java.util.Optional;

public interface AttachmentRepositoryCustom {

  Optional<Attachment> findAttachment(String noticeId, String attachmentId);
}
