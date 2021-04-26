package com.ask.sample.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = -7208631692505261616L;

  @Column
  @CreatedDate
  private LocalDateTime createdDt;

  @Column
  @LastModifiedDate
  private LocalDateTime modifiedDt;

  @Column
  @CreatedBy
  private String createdBy;

  @Column
  @LastModifiedBy
  private String modifiedBy;
}