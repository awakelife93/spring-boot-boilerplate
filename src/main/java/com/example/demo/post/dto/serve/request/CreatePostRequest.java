package com.example.demo.post.dto.serve.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

  @NotBlank(message = "field title is blank")
  @Size(max = 20, message = "field title is max size 20")
  @Schema(description = "Post Title", nullable = false)
  private String title;

  @NotBlank(message = "field subTitle is blank")
  @Size(max = 40, message = "field subTitle is max size 40")
  @Schema(description = "Post Sub Title", nullable = false)
  private String subTitle;

  @NotBlank(message = "field content is blank")
  @Size(max = 500, message = "field content is max size 500")
  @Schema(description = "Post Content", nullable = false)
  private String content;
}
