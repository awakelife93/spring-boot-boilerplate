package com.example.demo.service.post;

import com.example.demo.domain.post.dto.serve.GetExcludeUsersPostsRequest;
import com.example.demo.domain.post.dto.serve.GetPostResponse;
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
