package com.example.demo.post.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.common.security.SecurityItem;
import com.example.demo.common.security.WithMockCustomUser;
import com.example.demo.post.application.impl.ChangePostServiceImpl;
import com.example.demo.post.application.impl.GetPostServiceImpl;
import com.example.demo.post.dto.serve.request.CreatePostRequest;
import com.example.demo.post.dto.serve.request.GetExcludeUsersPostsRequest;
import com.example.demo.post.dto.serve.request.UpdatePostRequest;
import com.example.demo.post.dto.serve.response.CreatePostResponse;
import com.example.demo.post.dto.serve.response.GetPostResponse;
import com.example.demo.post.dto.serve.response.UpdatePostResponse;
import com.example.demo.post.entity.Post;
import com.example.demo.post.exception.PostNotFoundException;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("test")
@Tag("integration-test")
@DisplayName("integration - Post Controller Test")
@WebMvcTest(PostController.class)
@ExtendWith(MockitoExtension.class)
public class PostIntegrationControllerTests extends SecurityItem {

  @MockBean
  private GetPostServiceImpl getPostServiceImpl;

  @MockBean
  private ChangePostServiceImpl changePostServiceImpl;

  private final Post post = Instancio.create(Post.class);

  @BeforeEach
  void setUp() {
    mockMvc =
      MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .alwaysDo(print())
        .build();
  }

  @Nested
  @DisplayName("GET /api/v1/posts/{postId} Test")
  class GetPostByIdTest {

    @Test
    @DisplayName("GET /api/v1/posts/{postId} Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToGetPostResponse_when_GivenPostIdAndUserIsAuthenticated()
      throws Exception {
      when(getPostServiceImpl.getPostById(anyLong()))
        .thenReturn(GetPostResponse.of(post));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.postId").value(post.getId()))
        .andExpect(jsonPath("$.data.title").value(post.getTitle()))
        .andExpect(jsonPath("$.data.subTitle").value(post.getSubTitle()))
        .andExpect(jsonPath("$.data.content").value(post.getContent()))
        .andExpect(
          jsonPath("$.data.writer.userId").value(post.getUser().getId())
        );
    }

    @Test
    @DisplayName("Not Found Exception GET /api/v1/posts/{postId} Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToPostNotFoundException_when_GivenPostIdAndUserIsAuthenticated()
      throws Exception {
      PostNotFoundException postNotFoundException = new PostNotFoundException(
        post.getId()
      );

      when(getPostServiceImpl.getPostById(anyLong()))
        .thenThrow(postNotFoundException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(
          jsonPath("$.code").value(postNotFoundException.getCode().value())
        )
        .andExpect(
          jsonPath("$.message").value(postNotFoundException.getMessage())
        )
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("Unauthorized Exception GET /api/v1/posts/{postId} Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenPostIdAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("GET /api/v1/posts Test")
  class GetPostListTest {

    @Test
    @DisplayName("GET /api/v1/posts Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToListOfGetPostResponse_when_GivenDefaultPageableAndUserIsAuthenticated()
      throws Exception {
      when(getPostServiceImpl.getPostList(any(Pageable.class)))
        .thenReturn(List.of(GetPostResponse.of(post)));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.[0].postId").value(post.getId()))
        .andExpect(jsonPath("$.data.[0].title").value(post.getTitle()))
        .andExpect(jsonPath("$.data.[0].subTitle").value(post.getSubTitle()))
        .andExpect(jsonPath("$.data.[0].content").value(post.getContent()))
        .andExpect(
          jsonPath("$.data.[0].writer.userId").value(post.getUser().getId())
        );
    }

    @Test
    @DisplayName("Empty GET /api/v1/posts Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToListOfGetPostResponseIsEmpty_when_GivenDefaultPageableAndUserIsAuthenticated()
      throws Exception {
      when(getPostServiceImpl.getPostList(any(Pageable.class)))
        .thenReturn(List.of());

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Unauthorized Exception GET /api/v1/posts Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenDefaultPageableAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("GET /api/v1/posts/exclude-users Test")
  class getExcludeUsersPostListTest {

    GetExcludeUsersPostsRequest getExcludeUsersPostsRequest = Instancio.create(
      GetExcludeUsersPostsRequest.class
    );

    @Test
    @DisplayName("GET /api/v1/posts/exclude-users Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToListOfGetPostResponse_when_GivenDefaultPageableAndGetExcludeUsersPostsRequestAndUserIsAuthenticated()
      throws Exception {
      when(
        getPostServiceImpl.getExcludeUsersPostList(
          any(GetExcludeUsersPostsRequest.class),
          any(Pageable.class)
        )
      )
        .thenReturn(List.of(GetPostResponse.of(post)));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts/exclude-users")
            .with(csrf())
            .param(
              "userIds",
              objectMapper.writeValueAsString(
                getExcludeUsersPostsRequest.getUserIds().get(0)
              )
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.[0].postId").value(post.getId()))
        .andExpect(jsonPath("$.data.[0].title").value(post.getTitle()))
        .andExpect(jsonPath("$.data.[0].subTitle").value(post.getSubTitle()))
        .andExpect(jsonPath("$.data.[0].content").value(post.getContent()))
        .andExpect(
          jsonPath("$.data.[0].writer.userId").value(post.getUser().getId())
        );
    }

    @Test
    @DisplayName("Empty GET /api/v1/posts/exclude-users Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToListOfGetPostResponseIsEmpty_when_GivenDefaultPageableAndGetExcludeUsersPostsRequestAndUserIsAuthenticated()
      throws Exception {
      when(
        getPostServiceImpl.getExcludeUsersPostList(
          any(GetExcludeUsersPostsRequest.class),
          any(Pageable.class)
        )
      )
        .thenReturn(List.of());

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts/exclude-users")
            .with(csrf())
            .param(
              "userIds",
              objectMapper.writeValueAsString(
                getExcludeUsersPostsRequest.getUserIds().get(0)
              )
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName(
      "Unauthorized Exception GET /api/v1/posts/exclude-users Response"
    )
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenDefaultPageableAndGetExcludeUsersPostsRequestAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/posts/exclude-users")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("PUT /api/v1/posts Test")
  class CreatePostTest {

    CreatePostRequest createPostRequest = Instancio.create(
      CreatePostRequest.class
    );

    @Test
    @DisplayName("PUT /api/v1/posts Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToCreatePostResponse_when_GivenUserIdAndCreatePostRequestAndUserIsAuthenticated()
      throws Exception {
      when(
        changePostServiceImpl.createPost(
          any(CreatePostRequest.class),
          anyLong()
        )
      )
        .thenReturn(CreatePostResponse.of(post));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .put("/api/v1/posts")
            .with(csrf())
            .content(objectMapper.writeValueAsString(createPostRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.postId").value(post.getId()))
        .andExpect(jsonPath("$.data.title").value(post.getTitle()))
        .andExpect(jsonPath("$.data.subTitle").value(post.getSubTitle()))
        .andExpect(jsonPath("$.data.content").value(post.getContent()))
        .andExpect(
          jsonPath("$.data.writer.userId").value(post.getUser().getId())
        );
    }

    @Test
    @DisplayName("Field Valid Exception PUT /api/v1/posts Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToValidException_when_GivenUserIdAndWrongCreatePostRequestAndUserIsAuthenticated()
      throws Exception {
      CreatePostRequest createPostRequest = Instancio
        .of(CreatePostRequest.class)
        .set(Select.field(CreatePostRequest::getTitle), null)
        .set(Select.field(CreatePostRequest::getSubTitle), null)
        .create();

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .put("/api/v1/posts")
            .with(csrf())
            .content(objectMapper.writeValueAsString(createPostRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        // title:field title is blank, subTitle:field subTitle is blank,
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Unauthorized Exception PUT /api/v1/posts Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenUserIdAndCreatePostRequestAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .put("/api/v1/posts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("PATCH /api/v1/posts/{postId} Test")
  class UpdatePostTest {

    UpdatePostRequest updatePostRequest = Instancio.create(
      UpdatePostRequest.class
    );

    @Test
    @DisplayName("PATCH /api/v1/posts/{postId} Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToUpdatePostResponse_when_GivenPostIdAndUpdatePostRequestAndUserIsAuthenticated()
      throws Exception {
      when(
        changePostServiceImpl.updatePost(
          anyLong(),
          any(UpdatePostRequest.class)
        )
      )
        .thenReturn(UpdatePostResponse.of(post));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updatePostRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.postId").value(post.getId()))
        .andExpect(jsonPath("$.data.title").value(post.getTitle()))
        .andExpect(jsonPath("$.data.subTitle").value(post.getSubTitle()))
        .andExpect(jsonPath("$.data.content").value(post.getContent()))
        .andExpect(
          jsonPath("$.data.writer.userId").value(post.getUser().getId())
        );
    }

    @Test
    @DisplayName("Field Valid Exception PATCH /api/v1/posts/{postId} Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToValidException_when_GivenPostIdAndWrongUpdatePostRequestAndUserIsAuthenticated()
      throws Exception {
      UpdatePostRequest updatePostRequest = Instancio
        .of(UpdatePostRequest.class)
        .set(Select.field(UpdatePostRequest::getTitle), null)
        .set(Select.field(UpdatePostRequest::getSubTitle), null)
        .create();

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updatePostRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        // subTitle:field subTitle is blank, title:field title is blank,
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Unauthorized Exception PATCH /api/v1/posts/{postId} Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenPostIdAndUpdatePostRequestAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updatePostRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Not Found Exception PATCH /api/v1/posts/{postId} Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToPostNotFoundException_when_GivenPostIdAndUpdatePostRequestAndUserIsAuthenticated()
      throws Exception {
      PostNotFoundException postNotFoundException = new PostNotFoundException(
        post.getId()
      );

      when(
        changePostServiceImpl.updatePost(
          anyLong(),
          any(UpdatePostRequest.class)
        )
      )
        .thenThrow(postNotFoundException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updatePostRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(
          jsonPath("$.code").value(postNotFoundException.getCode().value())
        )
        .andExpect(
          jsonPath("$.message").value(postNotFoundException.getMessage())
        )
        .andExpect(jsonPath("$.errors").isEmpty());
    }
  }

  @Nested
  @DisplayName("DELETE /api/v1/posts/{postId} Test")
  class DeletePostTest {

    @Test
    @DisplayName("DELETE /api/v1/posts/{postId} Response")
    @WithMockCustomUser
    public void should_ExpectOKResponse_when_GivenPostIdAndUserIsAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .delete("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage));
    }

    @Test
    @DisplayName("Unauthorized Error DELETE /api/v1/posts/{postId} Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenPostIdAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .delete("/api/v1/posts/{postId}", post.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }
}
