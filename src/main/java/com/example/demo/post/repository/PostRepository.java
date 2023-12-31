package com.example.demo.post.repository;

import com.example.demo.post.entity.Post;
import com.example.demo.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository
  extends JpaRepository<Post, Long>, CustomPostRepository {
  Optional<Post> findOneById(Long postId);

  Optional<Post> findOneByUser(User user);
}
