package com.example.demo.repository.post.impl;

import static com.example.demo.domain.post.entity.QPost.post;

import com.example.demo.domain.post.dto.serve.GetExcludeUsersPostsRequest;
import com.example.demo.domain.post.dto.serve.GetPostResponse;
import com.example.demo.repository.post.CustomPostRepository;
import com.querydsl.core.types.Projections;
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
    GetExcludeUsersPostsRequest dto,
    Pageable pageable
  ) {
    return jpaQueryFactory
      .select(
        Projections.fields(
          GetPostResponse.class,
          post.id,
          post.content,
          post.subTitle,
          post.subTitle,
          post.title
        )
      )
      .from(post)
      .where(post.user.id.notIn(dto.getUserIds()))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetch();
  }
}
