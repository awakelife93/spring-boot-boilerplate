package com.example.demo.post.dto.serve.response;

import com.example.demo.post.dto.Writer;
import com.example.demo.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostResponse {

  @Schema(description = "Post Id", nullable = false)
  private Long postId;

  @Schema(description = "Post Title", nullable = false)
  private String title;

  @Schema(description = "Post Sub Title", nullable = false)
  private String subTitle;

  @Schema(description = "Post Content", nullable = false)
  private String content;

  @Schema(
    description = "Post Writer",
    nullable = false,
    implementation = Writer.class
  )
  private Writer writer;

  @Builder
  public CreatePostResponse(Post post) {
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

  public static CreatePostResponse of(Post post) {
    return builder().post(post).build();
  }
}
