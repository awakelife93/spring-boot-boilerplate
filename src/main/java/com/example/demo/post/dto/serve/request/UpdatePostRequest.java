package com.example.demo.post.dto.serve.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePostRequest {

  @NotBlank(message = "field title is blank")
  @Schema(description = "Post Title", nullable = false)
  private String title;

  @NotBlank(message = "field subTitle is blank")
  @Schema(description = "Post Sub Title", nullable = false)
  private String subTitle;

  @NotBlank(message = "field content is blank")
  @Schema(description = "Post Content", nullable = false)
  private String content;
}
