package com.example.demo.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

  private final JobRegistry jobRegistry;

  @Bean
  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
    JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
    jobProcessor.setJobRegistry(jobRegistry);
    return jobProcessor;
  }
}
