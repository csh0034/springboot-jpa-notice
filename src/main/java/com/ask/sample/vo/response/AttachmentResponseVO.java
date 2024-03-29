package com.ask.sample.vo.response;

import com.ask.sample.domain.Attachment;
import com.ask.sample.util.DateUtils;
import com.ask.sample.util.StringUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AttachmentResponseVO implements Serializable {

  private static final long serialVersionUID = -2551620172313279020L;

  private String id;

  private String fileNm;

  private String contentType;

  private Long downloadCnt;

  private String fileUrl;

  private String createdBy;

  private LocalDateTime createdDt;

  public static AttachmentResponseVO of(Attachment attachment, String fileUrl) {
    AttachmentResponseVO vo = new AttachmentResponseVO();
    vo.id = attachment.getId();
    vo.fileNm = attachment.getFileNm();
    vo.contentType = attachment.getContentType();
    vo.downloadCnt = attachment.getDownloadCnt();
    vo.fileUrl = fileUrl;
    vo.createdBy = attachment.getCreatedBy();
    vo.createdDt = attachment.getCreatedDt();
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