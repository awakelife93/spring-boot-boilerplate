package com.example.demo.post.api;

import com.example.demo.common.response.ErrorResponse;
import com.example.demo.post.application.ChangePostService;
import com.example.demo.post.application.GetPostService;
import com.example.demo.post.dto.serve.request.CreatePostRequest;
import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.request.UpdatePostRequest;
import com.example.demo.post.dto.serve.response.CreatePostResponse;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import com.example.demo.post.dto.serve.response.UpdatePostResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post", description = "Post API")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

  private final GetPostService getPostService;
  private final ChangePostService changePostService;

  @Operation(
    operationId = "createPost",
    summary = "Create Post",
    description = "Create Post API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "Create Post",
        content = @Content(
          schema = @Schema(implementation = CreatePostResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @PutMapping
  public ResponseEntity<CreatePostResponse> createPost(
    @RequestBody @Valid CreatePostRequest dto,
    @CurrentUser SecurityUserItem securityUserItem
  ) {
    final CreatePostResponse createPostResponse = changePostService.createPost(
      dto,
      securityUserItem.getUserId()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(createPostResponse);
  }

  @Operation(
    operationId = "getPostList",
    summary = "Get Post List",
    description = "Get Post List API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          array = @ArraySchema(
            schema = @Schema(implementation = GetPostResponse.class)
          )
        )
      ),
    }
  )
  @GetMapping
  public ResponseEntity<List<GetPostResponse>> getPostList(Pageable pageable) {
    final List<GetPostResponse> postResponses = getPostService.getPostList(
      pageable
    );
    return ResponseEntity.ok(postResponses);
  }

  @Operation(
    operationId = "getExcludeUsersPosts",
    summary = "Get Exclude Users By Post List",
    description = "Get Exclude Users By Post List API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          array = @ArraySchema(
            schema = @Schema(implementation = GetPostResponse.class)
          )
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @GetMapping("/exclude-users")
  public ResponseEntity<List<GetPostResponse>> getExcludeUsersPosts(
    @Valid GetExcludeUsersPostsRequest dto,
    Pageable pageable
  ) {
    final List<GetPostResponse> postResponses = getPostService.getExcludeUsersPosts(
      dto,
      pageable
    );
    return ResponseEntity.ok(postResponses);
  }

  @Operation(
    operationId = "getPostById",
    summary = "Get Post",
    description = "Get Post By Post Id API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          array = @ArraySchema(
            schema = @Schema(implementation = GetPostResponse.class)
          )
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @GetMapping("/{postId}")
  public ResponseEntity<GetPostResponse> getPostById(
    @PathVariable("postId") Long postId
  ) {
    final GetPostResponse postResponses = getPostService.getPostById(postId);
    return ResponseEntity.ok(postResponses);
  }

  @Operation(
    operationId = "updatePost",
    summary = "Update Post",
    description = "Update Post API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
          schema = @Schema(implementation = UpdatePostResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Post Not Found",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @PatchMapping("/{postId}")
  public ResponseEntity<UpdatePostResponse> updatePost(
    @RequestBody @Valid UpdatePostRequest dto,
    @PathVariable("postId") Long postId
  ) {
    final UpdatePostResponse updatePostResponse = changePostService.updatePost(
      postId,
      dto
    );
    return ResponseEntity.ok(updatePostResponse);
  }

  @Operation(
    operationId = "deletePost",
    summary = "Delete Post",
    description = "Delete Post API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "204", description = "Delete Post"),
      @ApiResponse(
        responseCode = "401",
        description = "Full authentication is required to access this resource",
        content = @Content(
          schema = @Schema(implementation = ErrorResponse.class)
        )
      ),
    }
  )
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
    changePostService.deletePost(postId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
