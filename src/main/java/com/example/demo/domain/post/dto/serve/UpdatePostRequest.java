package com.example.demo.domain.post.dto.serve;

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
  private String title;

  @NotBlank(message = "field subTitle is blank")
  private String subTitle;

  @NotBlank(message = "field content is blank")
  private String content;
}
