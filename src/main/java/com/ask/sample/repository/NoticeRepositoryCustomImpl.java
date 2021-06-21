package com.ask.sample.repository;

import com.ask.sample.util.StringUtils;
import com.ask.sample.vo.response.NoticeResponseVO;
import com.ask.sample.vo.response.QNoticeResponseVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.ask.sample.domain.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NoticeResponseVO> findAllNotice(String title, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotBlank(title)) {
            builder.and(notice.title.contains(title));
        }

        QueryResults<NoticeResponseVO> result = queryFactory
                .select(new QNoticeResponseVO(
                        notice.id,
                        notice.title,
                        notice.content,
                        notice.readCnt,
                        notice.attachments.size(),
                        notice.createdDt,
                        notice.createdBy))
                .from(notice)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.createdDt.desc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
