package com.example.demo.service.post.impl;

import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.user.exception.UserNotFoundException;
import com.example.demo.repository.post.PostRepository;
import com.example.demo.service.post.PostService;
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
      .orElseThrow(() -> new UserNotFoundException());

    return post;
  }
}
