package com.example.demo.post.application;

import com.example.demo.post.dto.serve.request.CreatePostRequest;
import com.example.demo.post.dto.serve.request.UpdatePostRequest;
import com.example.demo.post.dto.serve.response.CreatePostResponse;
import com.example.demo.post.dto.serve.response.UpdatePostResponse;

public interface ChangePostService {
  public UpdatePostResponse updatePost(Long postId, UpdatePostRequest dto);

  public CreatePostResponse createPost(CreatePostRequest dto, Long userId);

  public void deletePost(Long postId);
}
