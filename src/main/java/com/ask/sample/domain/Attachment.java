package com.ask.sample.domain;

import com.ask.sample.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tb_attachment")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Attachment extends BaseEntity {

    private static final long serialVersionUID = 6172416700186756912L;

    @Id
    @Column(name = "att_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String fileNm;

    private String contentType;

    private long fileSize;

    private long downloadCnt;

    private String savedFileDir;

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public static Attachment createAttachment(MultipartFile multipartFile, String uploadDir) {
        Attachment attachment = new Attachment();
        attachment.id = StringUtils.getNewId("att-");
        attachment.fileNm = multipartFile.getOriginalFilename();
        attachment.contentType = multipartFile.getContentType();
        attachment.fileSize = multipartFile.getSize();
        attachment.downloadCnt = 0;
        attachment.savedFileDir = uploadDir + System.getProperty("file.separator") + attachment.id;
        return attachment;
    }
}