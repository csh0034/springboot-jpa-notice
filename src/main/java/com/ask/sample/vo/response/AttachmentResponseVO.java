package com.ask.sample.vo.response;

import com.ask.sample.constant.Constants;
import com.ask.sample.domain.Attachment;
import com.ask.sample.util.StringUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class AttachmentResponseVO implements Serializable {

  private static final long serialVersionUID = -2551620172313279020L;

  private String id;

  private String fileNm;

  private String contentType;

  private Long downloadCnt;

  private String fileUrl;

  private String createdBy;

  private LocalDateTime createdDt;

  public static AttachmentResponseVO from(Attachment attachment, String fileUrl) {
    AttachmentResponseVO vo = new AttachmentResponseVO();
    vo.id = attachment.getId();
    vo.fileNm = attachment.getFileNm();
    vo.contentType = attachment.getContentType();
    vo.downloadCnt = attachment.getDownloadCnt();
    vo.fileUrl = fileUrl;
    vo.createdDt = attachment.getCreatedDt();
    vo.createdBy = attachment.getCreatedBy();
    return vo;
  }

  public String getCreatedBy() {
    return StringUtils.defaultString(createdBy);
  }

  public String getCreatedDt() {
    if (createdDt == null) {
      return "";
    }
    return createdDt.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
  }
}