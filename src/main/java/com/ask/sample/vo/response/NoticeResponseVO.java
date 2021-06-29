package com.ask.sample.vo.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class NoticeResponseVO extends BaseResponseVO {

  private String id;

  private String title;

  private String content;

  private Long readCnt;

  private int fileCnt;

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
}