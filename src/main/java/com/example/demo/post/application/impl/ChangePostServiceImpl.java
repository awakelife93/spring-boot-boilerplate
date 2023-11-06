package com.example.demo.post.application.impl;

import com.example.demo.post.application.ChangePostService;
import com.example.demo.post.application.PostService;
import com.example.demo.post.dto.serve.request.CreatePostRequest;
import com.example.demo.post.dto.serve.request.UpdatePostRequest;
import com.example.demo.post.dto.serve.response.CreatePostResponse;
import com.example.demo.post.dto.serve.response.UpdatePostResponse;
import com.example.demo.post.entity.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.application.UserService;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangePostServiceImpl implements ChangePostService {

  private final PostRepository postRepository;
  private final PostService postService;
  private final UserService userService;

  @Override
  public CreatePostResponse createPost(CreatePostRequest dto, Long userId) {
    final User user = userService.validateReturnUser(userId);
    final Post post = Post.toEntity(
      dto.getTitle(),
      dto.getSubTitle(),
      dto.getContent(),
      user
    );
    return CreatePostResponse.of(postRepository.save(post));
  }

  @Override
  public UpdatePostResponse updatePost(Long postId, UpdatePostRequest dto) {
    final Post post = postService.validateReturnPost(postId).update(dto);
    return UpdatePostResponse.of(post);
  }

  @Override
  public void deletePost(Long postId) {
    postRepository.deleteById(postId);
  }
}
