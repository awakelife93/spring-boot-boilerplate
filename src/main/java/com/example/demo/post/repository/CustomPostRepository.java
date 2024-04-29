package com.example.demo.post.repository;

import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
  List<GetPostResponse> getExcludeUsersPosts(
    GetExcludeUsersPostsRequest getExcludeUsersPostsRequest,
    Pageable pageable
  );
}
