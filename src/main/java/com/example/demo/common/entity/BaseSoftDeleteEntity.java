package com.example.demo.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseSoftDeleteEntity extends BaseEntity {

  @Column(nullable = false)
  private boolean isDeleted = Boolean.FALSE;
}
