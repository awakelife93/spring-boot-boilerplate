package com.example.demo.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.demo.post.application.impl.GetPostServiceImpl;
import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import com.example.demo.post.entity.Post;
import com.example.demo.post.exception.PostNotFoundException;
import com.example.demo.post.repository.PostRepository;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Get Post Service Test")
@ExtendWith(MockitoExtension.class)
public class GetPostServiceTests {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private GetPostServiceImpl getPostServiceImpl;

  private final Post post = Instancio.create(Post.class);

  private final Pageable defaultPageable = Pageable.ofSize(1);

  @Nested
  @DisplayName("Get Post By Id Test")
  class GetPostByIdTest {

    @Test
    @DisplayName("Success get post by id")
    public void should_AssertGetPostResponse_when_GivenPostId() {
      when(postRepository.findOneById(anyLong())).thenReturn(Optional.of(post));

      GetPostResponse getPostResponse = getPostServiceImpl.getPostById(
        post.getId()
      );

      assertNotNull(getPostResponse);
      assertEquals(post.getId(), getPostResponse.getPostId());
      assertEquals(post.getTitle(), getPostResponse.getTitle());
      assertEquals(post.getSubTitle(), getPostResponse.getSubTitle());
      assertEquals(post.getContent(), getPostResponse.getContent());
      assertEquals(
        post.getUser().getId(),
        getPostResponse.getWriter().getUserId()
      );
    }

    @Test
    @DisplayName("Not found post")
    public void should_AssertPostNotFoundException_when_GivenPostId() {
      when(postRepository.findOneById(anyLong()))
        .thenThrow(new PostNotFoundException(post.getId()));

      assertThrows(
        PostNotFoundException.class,
        () -> getPostServiceImpl.getPostById(post.getId())
      );
    }
  }

  @Nested
  @DisplayName("Get Post List Test")
  class GetPostListTest {

    @Test
    @DisplayName("Success get post list")
    public void should_AssertListOfGetPostResponse_when_GivenDefaultPageable() {
      Page<Post> postList = new PageImpl<>(List.of(post));
      when(postRepository.findAll(any(Pageable.class))).thenReturn(postList);

      List<GetPostResponse> getPostResponseList = getPostServiceImpl.getPostList(
        defaultPageable
      );

      assertThat(getPostResponseList).isNotEmpty();
      assertEquals(post.getId(), getPostResponseList.get(0).getPostId());
      assertEquals(post.getTitle(), getPostResponseList.get(0).getTitle());
      assertEquals(
        post.getSubTitle(),
        getPostResponseList.get(0).getSubTitle()
      );
      assertEquals(post.getContent(), getPostResponseList.get(0).getContent());
      assertEquals(
        post.getUser().getId(),
        getPostResponseList.get(0).getWriter().getUserId()
      );
    }

    @Test
    @DisplayName("Get post list is empty")
    public void should_AssertListOfGetPostResponseIsEmpty_when_GivenDefaultPageable() {
      Page<Post> emptyPostList = new PageImpl<>(List.of());

      when(postRepository.findAll(any(Pageable.class)))
        .thenReturn(emptyPostList);

      List<GetPostResponse> getPostResponseList = getPostServiceImpl.getPostList(
        defaultPageable
      );

      assertThat(getPostResponseList).isEmpty();
      assertEquals(getPostResponseList.size(), 0);
    }
  }

  @Nested
  @DisplayName("Get Exclude Users Post List Test")
  class GetExcludeUsersPostsTest {

    GetExcludeUsersPostsRequest getExcludeUsersPostsRequest = Instancio.create(
      GetExcludeUsersPostsRequest.class
    );

    @Test
    @DisplayName("Success get exclude users post list")
    public void should_AssertListOfGetPostResponse_when_GivenDefaultPageableAndGetExcludeUsersPostsRequest() {
      GetPostResponse getPostResponse = GetPostResponse.of(post);

      when(
        postRepository.getExcludeUsersPosts(
          any(GetExcludeUsersPostsRequest.class),
          any(Pageable.class)
        )
      )
        .thenReturn(List.of(getPostResponse));

      List<GetPostResponse> getPostResponseList = getPostServiceImpl.getExcludeUsersPostList(
        getExcludeUsersPostsRequest,
        defaultPageable
      );

      assertThat(getPostResponseList).isNotEmpty();
      assertEquals(post.getId(), getPostResponseList.get(0).getPostId());
      assertEquals(post.getTitle(), getPostResponseList.get(0).getTitle());
      assertEquals(
        post.getSubTitle(),
        getPostResponseList.get(0).getSubTitle()
      );
      assertEquals(post.getContent(), getPostResponseList.get(0).getContent());
      assertEquals(
        post.getUser().getId(),
        getPostResponseList.get(0).getWriter().getUserId()
      );
    }

    @Test
    @DisplayName("Get get exclude users post list is empty")
    public void should_AssertListOfGetPostResponseIsEmpty_when_GivenDefaultPageableAndGetExcludeUsersPostsRequest() {
      when(
        postRepository.getExcludeUsersPosts(
          any(GetExcludeUsersPostsRequest.class),
          any(Pageable.class)
        )
      )
        .thenReturn(List.of());

      List<GetPostResponse> getPostResponseList = getPostServiceImpl.getExcludeUsersPostList(
        getExcludeUsersPostsRequest,
        defaultPageable
      );

      assertThat(getPostResponseList).isEmpty();
      assertEquals(getPostResponseList.size(), 0);
    }
  }
}
