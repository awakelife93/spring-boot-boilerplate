package com.example.demo.domain.post.dto.serve;

import com.example.demo.domain.post.dto.Writer;
import com.example.demo.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class GetPostResponse {

  private Long postId;

  private String title;

  private String subTitle;

  private String content;

  private Writer writer;

  @Builder
  public GetPostResponse(Post post) {
    this.postId = post.getId();
    this.title = post.getTitle();
    this.subTitle = post.getSubTitle();
    this.content = post.getContent();
    this.writer =
      Writer.of(
        post.getUser().getId(),
        post.getUser().getEmail(),
        post.getUser().getName()
      );
  }

  public static GetPostResponse of(Post post) {
    return GetPostResponse.builder().post(post).build();
  }
}
