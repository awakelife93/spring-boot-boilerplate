package com.example.demo.controller;

import com.example.demo.domain.post.dto.serve.CreatePostRequest;
import com.example.demo.domain.post.dto.serve.CreatePostResponse;
import com.example.demo.domain.post.dto.serve.GetExcludeUsersPostsRequest;
import com.example.demo.domain.post.dto.serve.GetPostResponse;
import com.example.demo.domain.post.dto.serve.UpdatePostRequest;
import com.example.demo.domain.post.dto.serve.UpdatePostResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.annotation.CurrentUser;
import com.example.demo.service.post.ChangePostService;
import com.example.demo.service.post.GetPostService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

  private final GetPostService getPostService;
  private final ChangePostService changePostService;

  @PutMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreatePostResponse createPost(
    @RequestBody @Valid CreatePostRequest dto,
    @CurrentUser SecurityUserItem securityUserItem
  ) {
    return changePostService.createPost(dto, securityUserItem.getUserId());
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<GetPostResponse>> getPosts(Pageable pageable) {
    final List<GetPostResponse> postResponses = getPostService.getPosts(
      pageable
    );
    return ResponseEntity.ok(postResponses);
  }

  @GetMapping("/exclude-users")
  @ResponseStatus(HttpStatus.OK)
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

  @GetMapping("/{postId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<GetPostResponse> getPostById(
    @PathVariable("postId") Long postId
  ) {
    final GetPostResponse postResponses = getPostService.getPostById(postId);
    return ResponseEntity.ok(postResponses);
  }

  @PatchMapping("/{postId}")
  @ResponseStatus(HttpStatus.OK)
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

  @DeleteMapping("/{postId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("postId") Long postId) {
    changePostService.deletePost(postId);
  }
}
