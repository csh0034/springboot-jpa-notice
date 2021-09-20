package com.ask.sample.domain;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.ask.sample.util.IdGenerator;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "tb_notice", indexes = {
    @Index(name = "IDX_NOTICE_TITLE", columnList = "title")
})
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
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
  @EqualsAndHashCode.Include
  private String id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OrderBy(value = "createdDt DESC")
  @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Attachment> attachments = new ArrayList<>();

  private String title;

  @Lob
  private String content;

  private Long readCnt;

  public static Notice create(User user, String title, String content, List<Attachment> attachments) {
    Notice notice = new Notice();
    notice.user = user;
    notice.title = title;
    notice.content = content;
    notice.readCnt = 0L;
    attachments.forEach(notice::addAttachment);
    return notice;
  }

  public void update(String title, String content, List<Attachment> attachments) {
    this.title = title;
    this.content = content;
    attachments.forEach(this::addAttachment);
  }

  public void addAttachment(Attachment attachment) {
    attachments.add(attachment);
    attachment.setNotice(this);
  }

  public void increaseReadCnt() {
    this.readCnt++;
  }
}