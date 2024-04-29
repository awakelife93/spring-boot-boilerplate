package com.example.demo.post.application;

import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GetPostService {
  public GetPostResponse getPostById(Long postId);

  public List<GetPostResponse> getPostList(Pageable pageable);

  public List<GetPostResponse> getExcludeUsersPosts(
    GetExcludeUsersPostsRequest dto,
    Pageable pageable
  );
}
