package com.example.demo.post.repository.impl;

import static com.example.demo.post.entity.QPost.post;

import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import com.example.demo.post.repository.CustomPostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Transactional(readOnly = true)
  public List<GetPostResponse> getExcludeUsersPosts(
    GetExcludeUsersPostsRequest getExcludeUsersPostsRequest,
    Pageable pageable
  ) {
    return jpaQueryFactory
      .selectFrom(post)
      .where(post.user.id.notIn(getExcludeUsersPostsRequest.getUserIds()))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetch()
      .stream()
      .map(GetPostResponse::new)
      .toList();
  }
}
