package com.example.demo.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseChangerEntity extends BaseEntity {

  @CreatedBy
  @Column(nullable = false, updatable = false)
  protected Long createdBy;

  @LastModifiedBy
  @Column(nullable = false)
  protected Long updatedBy;
}
