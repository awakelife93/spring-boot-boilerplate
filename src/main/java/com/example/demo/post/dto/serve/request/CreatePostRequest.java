package com.example.demo.post.dto.serve.request;

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
  private String title;

  @NotBlank(message = "field subTitle is blank")
  @Size(max = 40, message = "field subTitle is max size 40")
  private String subTitle;

  @NotBlank(message = "field content is blank")
  @Size(max = 500, message = "field content is max size 500")
  private String content;
}
