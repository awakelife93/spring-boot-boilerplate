package com.example.demo.post.repository;

import com.example.demo.post.dto.serve.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.GetPostResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
  List<GetPostResponse> getExcludeUsersPosts(
    GetExcludeUsersPostsRequest dto,
    Pageable pageable
  );
}
