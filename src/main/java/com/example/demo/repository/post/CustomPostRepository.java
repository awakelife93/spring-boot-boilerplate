package com.example.demo.repository.post;

import com.example.demo.domain.post.dto.serve.GetExcludeUsersPostsRequest;
import com.example.demo.domain.post.dto.serve.GetPostResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
  List<GetPostResponse> getExcludeUsersPosts(
    GetExcludeUsersPostsRequest dto,
    Pageable pageable
  );
}
