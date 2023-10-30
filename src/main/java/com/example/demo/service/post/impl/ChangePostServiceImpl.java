package com.example.demo.service.post.impl;

import com.example.demo.domain.post.dto.serve.CreatePostRequest;
import com.example.demo.domain.post.dto.serve.CreatePostResponse;
import com.example.demo.domain.post.dto.serve.UpdatePostRequest;
import com.example.demo.domain.post.dto.serve.UpdatePostResponse;
import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.user.entity.User;
import com.example.demo.repository.post.PostRepository;
import com.example.demo.service.post.ChangePostService;
import com.example.demo.service.post.PostService;
import com.example.demo.service.user.UserService;
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
    return CreatePostResponse.builder().post(postRepository.save(post)).build();
  }

  @Override
  public UpdatePostResponse updatePost(Long postId, UpdatePostRequest dto) {
    final Post post = postService.validateReturnPost(postId).update(dto);
    return UpdatePostResponse.builder().post(post).build();
  }

  @Override
  public void deletePost(Long postId) {
    postRepository.deleteById(postId);
  }
}
