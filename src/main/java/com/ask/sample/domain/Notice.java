package com.ask.sample.domain;

import com.ask.sample.util.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "tb_notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notice extends BaseEntity {

    private static final long serialVersionUID = -1915060282725159757L;

    @Id
    @GenericGenerator(
            name = "noticeIdGenerator",
            strategy = "com.ask.sample.util.IdGenerator",
            parameters = @Parameter(name = IdGenerator.PARAM_KEY, value = "notice-")
    )
    @GeneratedValue(generator = "noticeIdGenerator")
    @Column(name = "notice_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @Column(unique = true)
    private String title;

    @Lob
    private String content;

    private long readCnt;

    public void addAttach(Attachment attachment) {
        attachments.add(attachment);
        attachment.setNotice(this);
    }

    public static Notice createNotice(User user, String title, String content, Attachment... attachments) {
        Notice notice = new Notice();
        notice.user = user;
        notice.title = title;
        notice.content = content;
        notice.readCnt = 0;
        for (Attachment attachment : attachments) {
            notice.addAttach(attachment);
        }
        return notice;
    }
}
