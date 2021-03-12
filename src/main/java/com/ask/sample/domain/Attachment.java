package com.ask.sample.domain;

import com.ask.sample.util.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "tb_attachment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attachment extends BaseEntity {

    private static final long serialVersionUID = 6172416700186756912L;

    @Id
    @GenericGenerator(
            name = "attIdGenerator",
            strategy = "com.ask.sample.util.IdGenerator",
            parameters = @Parameter(name = IdGenerator.PARAM_KEY, value = "att-")
    )
    @GeneratedValue(generator = "attIdGenerator")
    @Column(name = "att_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String fileNm;

    private String contentType;

    private long fileSize;

    private long downloadCnt;

    private String savedFilePath;

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public static Attachment createAttachment(MultipartFile multipartFile) {
        Attachment attachment = new Attachment();
        attachment.fileNm = multipartFile.getOriginalFilename();
        attachment.contentType = multipartFile.getContentType();
        attachment.fileSize = multipartFile.getSize();
        attachment.downloadCnt = 0;
        return attachment;
    }
}
