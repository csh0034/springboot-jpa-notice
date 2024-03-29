package com.ask.sample.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = -7208631692505261616L;

  @Column
  @CreatedDate
  protected LocalDateTime createdDt;

  @Column
  @LastModifiedDate
  protected LocalDateTime modifiedDt;

  @Column(length = 50)
  @CreatedBy
  protected String createdBy;

  @Column(length = 50)
  @LastModifiedBy
  protected String modifiedBy;
}