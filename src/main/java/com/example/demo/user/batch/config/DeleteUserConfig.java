package com.example.demo.user.batch.config;

import com.example.demo.user.batch.mapper.DeleteUserItem;
import com.example.demo.user.batch.mapper.DeleteUserItemRowMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DeleteUserConfig extends DefaultBatchConfiguration {

  private final JdbcTemplate jdbcTemplate;
  private final DataSource dataSource;
  private final int chunkSize = 10;

  @Bean
  public Job deleteUser(
    JobRepository jobRepository,
    PlatformTransactionManager transactionManager
  ) throws Exception {
    return new JobBuilder("deleteUserJob", jobRepository)
      .flow(generateStep(jobRepository, transactionManager))
      .end()
      .build();
  }

  @Bean
  @JobScope
  Step generateStep(
    JobRepository jobRepository,
    PlatformTransactionManager transactionManager
  ) throws Exception {
    return new StepBuilder("deleteUserStep", jobRepository)
      .<DeleteUserItem, DeleteUserItem>chunk(chunkSize, transactionManager)
      .allowStartIfComplete(true)
      .reader(reader(null))
      .writer(writer())
      .build();
  }

  @Bean
  @StepScope
  public JdbcPagingItemReader<DeleteUserItem> reader(
    @Value("#{jobParameters[now]}") LocalDateTime now
  ) throws Exception {
    return new JdbcPagingItemReaderBuilder<DeleteUserItem>()
      .name("DeletedUsersYearAgoReader")
      .dataSource(dataSource)
      .pageSize(chunkSize)
      .fetchSize(chunkSize)
      .queryProvider(pagingQueryProvider())
      .parameterValues(
        Collections.singletonMap("oneYearBeforeNow", now.minusYears(1))
      )
      .rowMapper(new DeleteUserItemRowMapper())
      .build();
  }

  @Bean
  @StepScope
  public ItemWriter<DeleteUserItem> writer() {
    return items -> {
      for (DeleteUserItem item : items) {
        log.info(
          "Hard Deleted User By = {} {} {} {} {}",
          item.getId(),
          item.getEmail(),
          item.getName(),
          item.getRole(),
          item.getDeletedDt()
        );

        jdbcTemplate.update(
          "DELETE FROM \"user\" WHERE user_id = ?",
          item.getId()
        );
      }
    };
  }

  @Bean
  public PagingQueryProvider pagingQueryProvider() throws Exception {
    SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();

    queryProvider.setDataSource(dataSource);
    queryProvider.setSelectClause("select *");
    queryProvider.setFromClause("from \"user\"");
    queryProvider.setWhereClause("where deleted_dt <= :oneYearBeforeNow");

    Map<String, Order> sortKeys = new HashMap<>(1);
    sortKeys.put("user_id", Order.ASCENDING);

    queryProvider.setSortKeys(sortKeys);

    return queryProvider.getObject();
  }
}
