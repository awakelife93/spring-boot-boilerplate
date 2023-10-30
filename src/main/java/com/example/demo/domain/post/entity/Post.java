package com.example.demo.domain.post.entity;

import com.example.demo.common.entity.BaseChangerEntity;
import com.example.demo.domain.post.dto.serve.UpdatePostRequest;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@Table(name = "post")
@AttributeOverride(name = "id", column = @Column(name = "post_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseChangerEntity {

  @Column(nullable = false, length = 20)
  private String title;

  @Column(nullable = false, length = 40)
  private String subTitle;

  @Column(nullable = false, length = 500)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  private Post(
    @NonNull String title,
    @NonNull String subTitle,
    @NonNull String content,
    @NonNull User user
  ) {
    this.title = title;
    this.subTitle = subTitle;
    this.content = content;
    this.user = user;
  }

  public Post update(UpdatePostRequest dto) {
    this.title = dto.getTitle();
    this.subTitle = dto.getSubTitle();
    this.content = dto.getContent();
    return this;
  }
}
