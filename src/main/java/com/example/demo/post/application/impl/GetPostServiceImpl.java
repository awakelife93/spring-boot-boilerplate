package com.example.demo.post.application.impl;

import com.example.demo.post.application.GetPostService;
import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import com.example.demo.post.entity.Post;
import com.example.demo.post.repository.PostRepository;
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

    return GetPostResponse.of(post);
  }

  @Override
  public List<GetPostResponse> getPostList(Pageable pageable) {
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
