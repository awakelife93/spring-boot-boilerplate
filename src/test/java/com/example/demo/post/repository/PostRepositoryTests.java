package com.example.demo.post.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.common.config.JpaAuditConfig;
import com.example.demo.common.config.QueryDslConfig;
import com.example.demo.common.security.WithMockCustomUser;
import com.example.demo.post.dto.serve.request.UpdatePostRequest;
import com.example.demo.post.entity.Post;
import com.example.demo.user.constant.UserRole;
import com.example.demo.user.entity.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Post Repository Test")
@Import(value = { QueryDslConfig.class, JpaAuditConfig.class })
@DataJpaTest
public class PostRepositoryTests {

  @Autowired
  private PostRepository postRepository;

  private Post postEntity;

  private final String defaultPostTitle = "unit test";

  private final String defaultPostSubTitle = "Post Repository Test";

  private final String defaultPostContent =
    "default value for post repository testing";

  private final String defaultUserEmail = "awakelife93@gmail.com";

  private final String defaultUserPassword = "test_password_123!@";

  private final String defaultUserName = "Hyunwoo Park";

  private final UserRole defaultUserRole = UserRole.USER;

  @BeforeEach
  public void setUp() throws Exception {
    postEntity =
      Post.toEntity(
        defaultPostTitle,
        defaultPostSubTitle,
        defaultPostContent,
        User.toEntity(
          defaultUserEmail,
          defaultUserName,
          defaultUserPassword,
          defaultUserRole
        )
      );
  }

  @Test
  @DisplayName("Create post")
  @WithMockCustomUser
  void should_AssertCreatedPostEntity_when_GivenPostEntity() {
    Post createPost = postRepository.save(postEntity);

    assertEquals(createPost.getId(), postEntity.getId());
    assertEquals(createPost.getTitle(), postEntity.getTitle());
    assertEquals(createPost.getSubTitle(), postEntity.getSubTitle());
    assertEquals(createPost.getContent(), postEntity.getContent());
    assertEquals(createPost.getUser().getId(), postEntity.getUser().getId());
  }

  @Test
  @DisplayName("Update post")
  @WithMockCustomUser
  void should_AssertUpdatedPostEntity_when_GivenPostIdAndUpdatePostRequest() {
    UpdatePostRequest updatePostRequest = Instancio.create(
      UpdatePostRequest.class
    );

    Post beforeUpdatePost = postRepository.save(postEntity);

    postRepository.save(
      beforeUpdatePost.update(
        updatePostRequest.getTitle(),
        updatePostRequest.getSubTitle(),
        updatePostRequest.getContent()
      )
    );

    Post afterUpdatePost = postRepository
      .findOneById(beforeUpdatePost.getId())
      .get();

    assertEquals(afterUpdatePost.getTitle(), updatePostRequest.getTitle());
    assertEquals(
      afterUpdatePost.getSubTitle(),
      updatePostRequest.getSubTitle()
    );
    assertEquals(afterUpdatePost.getContent(), updatePostRequest.getContent());
  }

  @Test
  @DisplayName("Delete post")
  @WithMockCustomUser
  void should_AssertDeletedPostEntity_when_GivenPostId() {
    Post beforeDeletePost = postRepository.save(postEntity);

    postRepository.deleteById(beforeDeletePost.getId());

    Post afterDeletePost = postRepository
      .findOneById(beforeDeletePost.getId())
      .orElse(null);

    assertNull(afterDeletePost);
  }

  @Test
  @DisplayName("Find post by id")
  @WithMockCustomUser
  void should_AssertFindPostEntity_when_GivenPostId() {
    Post beforeFindPost = postRepository.save(postEntity);

    Post afterFindPost = postRepository
      .findOneById(beforeFindPost.getId())
      .get();

    assertEquals(beforeFindPost.getId(), afterFindPost.getId());
    assertEquals(beforeFindPost.getTitle(), afterFindPost.getTitle());
    assertEquals(beforeFindPost.getSubTitle(), afterFindPost.getSubTitle());
    assertEquals(beforeFindPost.getContent(), afterFindPost.getContent());
    assertEquals(
      beforeFindPost.getUser().getId(),
      afterFindPost.getUser().getId()
    );
  }

  @Test
  @DisplayName("Find post by user")
  @WithMockCustomUser
  void should_AssertFindPostEntity_when_GivenUser() {
    Post beforeFindPost = postRepository.save(postEntity);

    Post afterFindPost = postRepository
      .findOneByUser(beforeFindPost.getUser())
      .get();

    assertEquals(beforeFindPost.getId(), afterFindPost.getId());
    assertEquals(beforeFindPost.getTitle(), afterFindPost.getTitle());
    assertEquals(beforeFindPost.getSubTitle(), afterFindPost.getSubTitle());
    assertEquals(beforeFindPost.getContent(), afterFindPost.getContent());
    assertEquals(
      beforeFindPost.getUser().getId(),
      afterFindPost.getUser().getId()
    );
  }
}
