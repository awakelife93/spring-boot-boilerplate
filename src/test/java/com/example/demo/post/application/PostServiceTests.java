package com.example.demo.post.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.demo.post.application.impl.PostServiceImpl;
import com.example.demo.post.entity.Post;
import com.example.demo.post.exception.PostNotFoundException;
import com.example.demo.post.repository.PostRepository;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Post Service Test")
@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostServiceImpl postServiceImpl;

  private final Post post = Instancio.create(Post.class);

  @Nested
  @DisplayName("Validate And Return Post Entity Test")
  class ValidateReturnPostTest {

    @Test
    @DisplayName("Success validate and get post entity")
    public void should_AssertPostEntity_when_GivenPostId() {
      when(postRepository.findOneById(anyLong())).thenReturn(Optional.of(post));

      Post validatePost = postServiceImpl.validateReturnPost(post.getId());

      assertNotNull(validatePost);
      assertEquals(post.getId(), validatePost.getId());
      assertEquals(post.getTitle(), validatePost.getTitle());
      assertEquals(post.getSubTitle(), validatePost.getSubTitle());
      assertEquals(post.getContent(), validatePost.getContent());
      assertEquals(post.getUser().getId(), validatePost.getUser().getId());
    }

    @Test
    @DisplayName("validate and post entity is not found exception")
    public void should_AssertPostNotFoundException_when_GivenPostId() {
      when(postRepository.findOneById(anyLong())).thenReturn(Optional.empty());

      assertThrows(
        PostNotFoundException.class,
        () -> postServiceImpl.validateReturnPost(post.getId())
      );
    }
  }
}
