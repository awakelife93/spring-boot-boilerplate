package com.example.demo.user.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.common.config.TestBatchConfig;
import com.example.demo.user.batch.config.DeleteUserConfig;
import com.example.demo.user.batch.mapper.DeleteUserItem;
import com.example.demo.user.batch.mapper.DeleteUserItemRowMapper;
import com.example.demo.user.constant.UserRole;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("integration-test")
@DisplayName("integration - Delete User Config Batch Test")
@SpringBootTest(classes = { DeleteUserConfig.class, TestBatchConfig.class })
@SpringBatchTest
public class DeleteUserConfigIntegrationTests {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  private final String defaultUserEmail = "awakelife93@gmail.com";

  private final String defaultUserEncodePassword =
    "$2a$10$T44NRNpbxkQ9qHbCtqQZ7O3gYfipzC0cHvOIJ/aV4PTlvJjtDl7x2\n" + //
    "";

  private final String defaultUserName = "Hyunwoo Park";

  private final UserRole defaultUserRole = UserRole.USER;

  @AfterEach
  public void cleanUp() {
    jobRepositoryTestUtils.removeJobExecutions();
  }

  @Test
  @DisplayName("DeleteUserConfig batch Integration Test")
  public void should_AssertBatchStatusAndExitStatusAndListOfDeletedUser_when_GivenLocalDateTimeIsNowTime()
    throws Exception {
    LocalDateTime now = LocalDateTime.now().withNano(0);

    jdbcTemplate.update(
      "insert into \"user\" (created_dt, updated_dt, deleted_dt, email, name, password, role) values (?, ?, ?, ?, ?, ?, ?)",
      now,
      now,
      now.minusYears(1),
      defaultUserEmail,
      defaultUserName,
      defaultUserEncodePassword,
      defaultUserRole.name()
    );

    final JobParameters jobParameters = new JobParametersBuilder()
      .addLocalDateTime("now", LocalDateTime.now())
      .toJobParameters();

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    List<DeleteUserItem> deleteUserItems = jdbcTemplate.query(
      "select * from \"user\" where deleted_dt <= ?",
      new DeleteUserItemRowMapper(),
      now.minusYears(1)
    );

    assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    assertThat(deleteUserItems).isEmpty();
    assertEquals(deleteUserItems.size(), 0);
  }
}
