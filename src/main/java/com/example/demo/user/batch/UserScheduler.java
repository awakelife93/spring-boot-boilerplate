package com.example.demo.user.batch;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserScheduler {

  private final JobLauncher jobLauncher;
  private final JobRegistry jobRegistry;

  // 1 am
  @Scheduled(cron = "0 0 01 * * ?")
  public void run() {
    try {
      Job job = jobRegistry.getJob("deleteUserJob");
      JobParameters jobParameters = new JobParametersBuilder()
        .addLocalDateTime("now", LocalDateTime.now())
        .toJobParameters();

      jobLauncher.run(job, jobParameters);
    } catch (
      NoSuchJobException
      | JobInstanceAlreadyCompleteException
      | JobExecutionAlreadyRunningException
      | JobParametersInvalidException
      | JobRestartException exception
    ) {
      throw new RuntimeException(exception);
    }
  }
}
