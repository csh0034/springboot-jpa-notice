package com.ask.sample.repository;

import com.ask.sample.domain.Attachment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.ask.sample.domain.QAttachment.attachment;
import static com.ask.sample.domain.QNotice.notice;

@RequiredArgsConstructor
public class AttachmentRepositoryCustomImpl implements AttachmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Attachment> findAttachment(String noticeId, String attachmentId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(attachment)
                .innerJoin(attachment.notice, notice)
                .where(notice.id.eq(noticeId).and(attachment.id.eq(attachmentId)))
                .fetchOne()
        );
    }
}
