package com.example.demo.post.application;

import com.example.demo.post.dto.serve.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.GetPostResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GetPostService {
  public GetPostResponse getPostById(Long postId);

  public List<GetPostResponse> getPosts(Pageable pageable);

  public List<GetPostResponse> getExcludeUsersPosts(
    GetExcludeUsersPostsRequest dto,
    Pageable pageable
  );
}
