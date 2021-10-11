package com.ask.sample.vo.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;
import static java.util.stream.Collectors.toList;

import com.ask.sample.domain.Notice;
import com.ask.sample.util.DateUtils;
import com.ask.sample.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class NoticeResponseVO implements Serializable {

  private static final long serialVersionUID = -612696565073638129L;

  private String id;

  private String title;

  private String content;

  private Long readCnt;

  private int fileCnt;

  private String createdBy;

  private LocalDateTime createdDt;

  @JsonInclude(Include.NON_NULL)
  private List<AttachmentResponseVO> files;

  @QueryProjection
  public NoticeResponseVO(String id, String title, String content, Long readCnt, int fileCnt, LocalDateTime createdDt,
      String createdBy) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.readCnt = readCnt;
    this.fileCnt = fileCnt;
    this.createdDt = createdDt;
    this.createdBy = createdBy;
  }

  public static NoticeResponseVO of(Notice notice, String serverUrl) {
    NoticeResponseVO vo = new NoticeResponseVO();
    vo.id = notice.getId();
    vo.title = notice.getTitle();
    vo.content = notice.getContent();
    vo.readCnt = notice.getReadCnt();
    vo.fileCnt = notice.getAttachments().size();
    vo.createdBy = notice.getCreatedBy();
    vo.createdDt = notice.getCreatedDt();
    vo.files = notice.getAttachments().stream()
        .map(attachment -> {
          String fileUrl = String.format("%s/notice/%s/attachment/%s", serverUrl, notice.getId(), attachment.getId());
          return AttachmentResponseVO.of(attachment, fileUrl);
        }).collect(toList());

    return vo;
  }

  public String getCreatedBy() {
    return StringUtils.defaultString(createdBy);
  }

  public String getCreatedDt() {
    if (createdDt == null) {
      return "";
    }
    return createdDt.format(DateUtils.getDefaultTimeFormatter());
  }
}