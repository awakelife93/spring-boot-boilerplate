package com.example.demo.service.post.impl;

import com.example.demo.domain.post.dto.serve.GetExcludeUsersPostsRequest;
import com.example.demo.domain.post.dto.serve.GetPostResponse;
import com.example.demo.domain.post.entity.Post;
import com.example.demo.repository.post.PostRepository;
import com.example.demo.service.post.GetPostService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetPostServiceImpl implements GetPostService {

  private final PostRepository postRepository;

  @Override
  public GetPostResponse getPostById(Long postId) {
    final Post post = postRepository.findOneById(postId).orElse(null);

    if (Objects.isNull(post)) {
      return null;
    }

    return GetPostResponse.builder().post(post).build();
  }

  @Override
  public List<GetPostResponse> getPosts(Pageable pageable) {
    return postRepository
      .findAll(pageable)
      .getContent()
      .stream()
      .map(GetPostResponse::new)
      .collect(Collectors.toList());
  }

  @Override
  public List<GetPostResponse> getExcludeUsersPosts(
    GetExcludeUsersPostsRequest dto,
    Pageable pageable
  ) {
    return postRepository.getExcludeUsersPosts(dto, pageable);
  }
}
