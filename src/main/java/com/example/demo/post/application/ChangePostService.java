package com.example.demo.post.application;

import com.example.demo.post.dto.serve.CreatePostRequest;
import com.example.demo.post.dto.serve.CreatePostResponse;
import com.example.demo.post.dto.serve.UpdatePostRequest;
import com.example.demo.post.dto.serve.UpdatePostResponse;

public interface ChangePostService {
  public UpdatePostResponse updatePost(Long postId, UpdatePostRequest dto);

  public CreatePostResponse createPost(CreatePostRequest dto, Long userId);

  public void deletePost(Long postId);
}
