package com.example.demo.post.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.post.application.impl.ChangePostServiceImpl;
import com.example.demo.post.application.impl.PostServiceImpl;
import com.example.demo.post.dto.serve.request.CreatePostRequest;
import com.example.demo.post.dto.serve.request.UpdatePostRequest;
import com.example.demo.post.dto.serve.response.CreatePostResponse;
import com.example.demo.post.dto.serve.response.UpdatePostResponse;
import com.example.demo.post.entity.Post;
import com.example.demo.post.exception.PostNotFoundException;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.application.impl.UserServiceImpl;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserNotFoundException;
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
@DisplayName("Unit - Post / Put / Delete / Patch Post Service Test")
@ExtendWith(MockitoExtension.class)
public class ChangePostServiceTests {

  @Mock
  private UserServiceImpl userServiceImpl;

  @Mock
  private PostServiceImpl postServiceImpl;

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private ChangePostServiceImpl changePostServiceImpl;

  private final Post post = Instancio.create(Post.class);

  private final User user = Instancio.create(User.class);

  @Nested
  @DisplayName("Delete Post Test")
  class DeleteTest {

    @Test
    @DisplayName("Success delete post")
    public void should_VerifyCallDeleteByIdMethods_when_GivenPostId() {
      changePostServiceImpl.deletePost(post.getId());

      verify(postRepository, times(1)).deleteById(anyLong());
    }
  }

  @Nested
  @DisplayName("Update Post Test")
  class UpdateTest {

    UpdatePostRequest updatePostRequest = Instancio.create(
      UpdatePostRequest.class
    );

    @Test
    @DisplayName("Success update post")
    public void should_AssertUpdatePostResponse_when_GivenPostIdAndUpdatePostRequest() {
      when(postServiceImpl.validateReturnPost(anyLong())).thenReturn(post);

      UpdatePostResponse updatePostResponse = changePostServiceImpl.updatePost(
        post.getId(),
        updatePostRequest
      );

      assertNotNull(updatePostResponse);
      assertEquals(post.getId(), updatePostResponse.getPostId());
      assertEquals(post.getTitle(), updatePostResponse.getTitle());
      assertEquals(post.getSubTitle(), updatePostResponse.getSubTitle());
      assertEquals(post.getContent(), updatePostResponse.getContent());
      assertEquals(
        post.getUser().getId(),
        updatePostResponse.getWriter().getUserId()
      );
    }

    @Test
    @DisplayName("Not found post")
    public void should_AssertPostNotFoundException_when_GivenPostIdAndUpdatePostRequest() {
      when(postServiceImpl.validateReturnPost(anyLong()))
        .thenThrow(new PostNotFoundException(post.getId()));

      assertThrows(
        PostNotFoundException.class,
        () -> changePostServiceImpl.updatePost(post.getId(), updatePostRequest)
      );
    }
  }

  @Nested
  @DisplayName("Create Post Test")
  class CreatePostTest {

    CreatePostRequest createPostRequest = Instancio.create(
      CreatePostRequest.class
    );

    @Test
    @DisplayName("Success create post")
    public void should_AssertCreatePostResponse_when_GivenUserIdAndCreatePostRequest() {
      when(userServiceImpl.validateReturnUser(anyLong())).thenReturn(user);
      when(postRepository.save(any(Post.class))).thenReturn(post);

      CreatePostResponse createPostResponse = changePostServiceImpl.createPost(
        createPostRequest,
        user.getId()
      );

      assertNotNull(createPostResponse);
      assertEquals(post.getId(), createPostResponse.getPostId());
      assertEquals(post.getTitle(), createPostResponse.getTitle());
      assertEquals(post.getSubTitle(), createPostResponse.getSubTitle());
      assertEquals(post.getContent(), createPostResponse.getContent());
      assertEquals(
        post.getUser().getId(),
        createPostResponse.getWriter().getUserId()
      );
    }

    @Test
    @DisplayName("Not found user")
    public void should_AssertUserNotFoundException_when_GivenUserIdAndCreatePostRequest() {
      when(userServiceImpl.validateReturnUser(anyLong()))
        .thenThrow(new UserNotFoundException(user.getId()));

      assertThrows(
        UserNotFoundException.class,
        () -> changePostServiceImpl.createPost(createPostRequest, user.getId())
      );
    }
  }
}
