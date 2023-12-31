package com.example.demo.user.batch.config;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DeleteUserConfig extends DefaultBatchConfiguration {

  private final UserRepository userRepository;
  private List<User> users;

  @Bean
  public Job deleteUser(
    JobRepository jobRepository,
    Step findDeletedUsersYearAgoStep,
    Step deleteUsersStep
  ) {
    return new JobBuilder("deleteUserJob", jobRepository)
      .start(findDeletedUsersYearAgoStep)
      .next(deleteUsersStep)
      .build();
  }

  @Bean
  @JobScope
  public Step findDeletedUsersYearAgoStep(
    JobRepository jobRepository,
    Tasklet findDeletedUsersYearAgoTasklet,
    PlatformTransactionManager platformTransactionManager
  ) {
    return new StepBuilder("findDeletedUsersYearAgoStep", jobRepository)
      .tasklet(findDeletedUsersYearAgoTasklet, platformTransactionManager)
      .build();
  }

  @Bean
  @JobScope
  public Step deleteUsersStep(
    JobRepository jobRepository,
    Tasklet deleteUsersTasklet,
    PlatformTransactionManager platformTransactionManager
  ) {
    return new StepBuilder("deleteUsersStep", jobRepository)
      .tasklet(deleteUsersTasklet, platformTransactionManager)
      .build();
  }

  @Bean
  @StepScope
  public Tasklet findDeletedUsersYearAgoTasklet(
    @Value("#{jobParameters[now]}") String now
  ) {
    return (
      (contribution, chunkContext) -> {
        LocalDateTime localDateTime = LocalDateTime.parse(
          now,
          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        this.users = userRepository.findDeletedUsersYearAgo(localDateTime);

        return RepeatStatus.FINISHED;
      }
    );
  }

  @Bean
  @StepScope
  public Tasklet deleteUsersTasklet() {
    return (
      (contribution, chunkContext) -> {
        for (User user : this.users) {
          log.info(
            "Hard Deleted User By = {} {} {} {}",
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole().toString()
          );
          userRepository.hardDeleteUser(user.getId());
        }

        return RepeatStatus.FINISHED;
      }
    );
  }
}
