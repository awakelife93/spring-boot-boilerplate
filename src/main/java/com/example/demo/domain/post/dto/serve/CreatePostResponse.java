package com.example.demo.domain.post.dto.serve;

import com.example.demo.domain.post.dto.Writer;
import com.example.demo.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public class CreatePostResponse {

  private Long postId;

  private String title;

  private String subTitle;

  private String content;

  private Writer writer;

  @Builder
  public CreatePostResponse(Post post) {
    this.postId = post.getId();
    this.title = post.getTitle();
    this.subTitle = post.getSubTitle();
    this.content = post.getContent();
    this.writer =
      Writer
        .builder()
        .userId(post.getUser().getId())
        .email(post.getUser().getEmail())
        .name(post.getUser().getName())
        .build();
  }
}
