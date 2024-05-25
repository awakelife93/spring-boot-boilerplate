package com.example.demo.post.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.post.application.impl.ChangePostServiceImpl;
import com.example.demo.post.application.impl.GetPostServiceImpl;
import com.example.demo.post.dto.serve.request.CreatePostRequest;
import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.request.UpdatePostRequest;
import com.example.demo.post.dto.serve.response.CreatePostResponse;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import com.example.demo.post.dto.serve.response.UpdatePostResponse;
import com.example.demo.post.entity.Post;
import com.example.demo.security.SecurityUserItem;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Post Controller Test")
@ExtendWith(MockitoExtension.class)
public class PostControllerTests {

  @InjectMocks
  private PostController postController;

  @Mock
  private GetPostServiceImpl getPostServiceImpl;

  @Mock
  private ChangePostServiceImpl changePostServiceImpl;

  private final Post post = Instancio.create(Post.class);

  private final Pageable defaultPageable = Pageable.ofSize(1);

  @Test
  @DisplayName("Get post by id")
  public void should_AssertGetPostResponse_when_GivenPostId() {
    when(getPostServiceImpl.getPostById(anyLong()))
      .thenReturn(GetPostResponse.of(post));

    ResponseEntity<GetPostResponse> response = postController.getPostById(
      post.getId()
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    GetPostResponse body = response.getBody();
    assertEquals(post.getId(), body.getPostId());
    assertEquals(post.getTitle(), body.getTitle());
    assertEquals(post.getSubTitle(), body.getSubTitle());
    assertEquals(post.getContent(), body.getContent());
    assertEquals(post.getUser().getId(), body.getWriter().getUserId());
  }

  @Test
  @DisplayName("Get post list")
  public void should_AssertListOfGetPostResponse_when_GivenDefaultPageable() {
    when(getPostServiceImpl.getPostList(any(Pageable.class)))
      .thenReturn(List.of(GetPostResponse.of(post)));

    ResponseEntity<List<GetPostResponse>> response = postController.getPostList(
      defaultPageable
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<GetPostResponse> body = response.getBody();
    assertThat(body).isNotEmpty();
    assertEquals(post.getId(), body.get(0).getPostId());
    assertEquals(post.getTitle(), body.get(0).getTitle());
    assertEquals(post.getSubTitle(), body.get(0).getSubTitle());
    assertEquals(post.getContent(), body.get(0).getContent());
    assertEquals(post.getUser().getId(), body.get(0).getWriter().getUserId());
  }

  @Test
  @DisplayName("Get exclude users post list")
  public void should_AssertListOfGetPostResponse_when_GivenDefaultPageableAndGetExcludeUsersPostsRequest() {
    GetExcludeUsersPostsRequest getExcludeUsersPostsRequest = Instancio.create(
      GetExcludeUsersPostsRequest.class
    );

    when(
      getPostServiceImpl.getExcludeUsersPostList(
        any(GetExcludeUsersPostsRequest.class),
        any(Pageable.class)
      )
    )
      .thenReturn(List.of(GetPostResponse.of(post)));

    ResponseEntity<List<GetPostResponse>> response = postController.getExcludeUsersPostList(
      getExcludeUsersPostsRequest,
      defaultPageable
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<GetPostResponse> body = response.getBody();
    assertThat(body).isNotEmpty();
    assertEquals(post.getId(), body.get(0).getPostId());
    assertEquals(post.getTitle(), body.get(0).getTitle());
    assertEquals(post.getSubTitle(), body.get(0).getSubTitle());
    assertEquals(post.getContent(), body.get(0).getContent());
    assertEquals(post.getUser().getId(), body.get(0).getWriter().getUserId());
  }

  @Test
  @DisplayName("Create post")
  public void should_AssertCreatePostResponse_when_GivenUserIdAndCreatePostRequest() {
    CreatePostRequest createPostRequest = Instancio.create(
      CreatePostRequest.class
    );
    SecurityUserItem securityUserItem = Instancio.create(
      SecurityUserItem.class
    );

    when(
      changePostServiceImpl.createPost(any(CreatePostRequest.class), anyLong())
    )
      .thenReturn(CreatePostResponse.of(post));

    ResponseEntity<CreatePostResponse> response = postController.createPost(
      createPostRequest,
      securityUserItem
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    CreatePostResponse body = response.getBody();
    assertEquals(post.getId(), body.getPostId());
    assertEquals(post.getTitle(), body.getTitle());
    assertEquals(post.getSubTitle(), body.getSubTitle());
    assertEquals(post.getContent(), body.getContent());
    assertEquals(post.getUser().getId(), body.getWriter().getUserId());
  }

  @Test
  @DisplayName("Update post")
  public void should_AssertUpdatePostResponse_when_GivenPostIdAndUpdatePostRequest() {
    UpdatePostRequest updatePostRequest = Instancio.create(
      UpdatePostRequest.class
    );

    when(
      changePostServiceImpl.updatePost(anyLong(), any(UpdatePostRequest.class))
    )
      .thenReturn(UpdatePostResponse.of(post));

    ResponseEntity<UpdatePostResponse> response = postController.updatePost(
      updatePostRequest,
      post.getId()
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    UpdatePostResponse body = response.getBody();
    assertEquals(post.getId(), body.getPostId());
    assertEquals(post.getTitle(), body.getTitle());
    assertEquals(post.getSubTitle(), body.getSubTitle());
    assertEquals(post.getContent(), body.getContent());
    assertEquals(post.getUser().getId(), body.getWriter().getUserId());
  }

  @Test
  @DisplayName("Delete post")
  public void should_VerifyCallDeletePostMethod_when_GivenPostId() {
    ResponseEntity<Void> response = postController.deletePost(post.getId());

    assertNotNull(response);
    assertNull(response.getBody());
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    verify(changePostServiceImpl, times(1)).deletePost(anyLong());
  }
}
