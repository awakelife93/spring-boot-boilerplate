package com.example.demo.post.dto.serve.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetExcludeUsersPostsRequest {

  @NotNull(message = "field userIds is null")
  @Size(min = 1, message = "field userIds is empty")
  @Schema(description = "User Ids", nullable = false)
  private List<Long> userIds;
}
