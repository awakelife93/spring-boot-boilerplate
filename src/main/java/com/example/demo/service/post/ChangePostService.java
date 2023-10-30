package com.example.demo.service.post;

import com.example.demo.domain.post.dto.serve.CreatePostRequest;
import com.example.demo.domain.post.dto.serve.CreatePostResponse;
import com.example.demo.domain.post.dto.serve.UpdatePostRequest;
import com.example.demo.domain.post.dto.serve.UpdatePostResponse;

public interface ChangePostService {
  public UpdatePostResponse updatePost(Long postId, UpdatePostRequest dto);

  public CreatePostResponse createPost(CreatePostRequest dto, Long userId);

  public void deletePost(Long postId);
}
