package com.example.demo.post.application.impl;

import com.example.demo.post.application.PostService;
import com.example.demo.post.entity.Post;
import com.example.demo.post.exception.PostNotFoundException;
import com.example.demo.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  @Override
  public Post validateReturnPost(Long postId) {
    final Post post = postRepository
      .findOneById(postId)
      .orElseThrow(() -> new PostNotFoundException(postId));

    return post;
  }
}
