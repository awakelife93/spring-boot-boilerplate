package com.example.demo.repository.post;

import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository
  extends JpaRepository<Post, Long>, CustomPostRepository {
  Optional<Post> findOneById(Long postId);

  Optional<Post> findOneByUser(User user);
}
