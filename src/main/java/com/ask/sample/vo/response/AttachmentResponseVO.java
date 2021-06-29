package com.ask.sample.vo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class AttachmentResponseVO extends BaseResponseVO {

  private String id;

  private String fileNm;

  private String contentType;

  private Long downloadCnt;

  private String fileUrl;
}