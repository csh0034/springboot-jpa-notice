package com.ask.sample.domain;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.ask.sample.util.StringUtils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "tb_attachment")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Attachment extends BaseEntity {

  private static final long serialVersionUID = 6172416700186756912L;

  @Id
  @Column(name = "att_id")
  @EqualsAndHashCode.Include
  private String id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "notice_id")
  @ToString.Exclude
  private Notice notice;

  private String fileNm;

  private String contentType;

  private Long fileSize;

  private Long downloadCnt;

  private String savedFileDir;

  public void setNotice(Notice notice) {
    this.notice = notice;
  }

  public static Attachment create(MultipartFile multipartFile, String uploadDir) {
    Attachment attachment = new Attachment();
    attachment.id = StringUtils.getNewId("att-");
    attachment.fileNm = multipartFile.getOriginalFilename();
    attachment.contentType = multipartFile.getContentType();
    attachment.fileSize = multipartFile.getSize();
    attachment.downloadCnt = 0L;
    attachment.savedFileDir = uploadDir + System.getProperty("file.separator") + attachment.id;
    return attachment;
  }

  public void increaseDownloadCnt() {
    this.downloadCnt++;
  }
}