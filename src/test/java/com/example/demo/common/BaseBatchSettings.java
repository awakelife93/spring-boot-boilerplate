package com.example.demo.common;

import com.example.demo.common.config.QueryDslConfig;
import com.example.demo.post.entity.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;

@Import(QueryDslConfig.class)
@EnableAutoConfiguration
@EnableJpaRepositories(
  basePackageClasses = { UserRepository.class, PostRepository.class }
)
@EntityScan(basePackageClasses = { User.class, Post.class })
@SpringBatchTest
public class BaseBatchSettings {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  protected boolean isExecuteGeneratePostgresMetaTable = false;

  /**
   * @see <a href="https://github.com/spring-projects/spring-batch/blob/5.0.x/spring-batch-core/src/main/resources/org/springframework/batch/core/schema-postgresql.sql">https://github.com/spring-projects/spring-batch/blob/5.0.x/spring-batch-core/src/main/resources/org/springframework/batch/core/schema-postgresql.sql</a>
   */
  public void generatePostgresMetaTable() {
    jdbcTemplate.execute(
      String.format(
        "CREATE TABLE IF NOT EXISTS BATCH_JOB_INSTANCE  (\n" + //
        "    \tJOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,\n" + //
        "    \tVERSION BIGINT ,\n" + //
        "    \tJOB_NAME VARCHAR(100) NOT NULL,\n" + //
        "    \tJOB_KEY VARCHAR(32) NOT NULL,\n" + //
        "    \tconstraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)\n" + //
        "    )"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION  (\n" + //
        "    \tJOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,\n" + //
        "    \tVERSION BIGINT  ,\n" + //
        "    \tJOB_INSTANCE_ID BIGINT NOT NULL,\n" + //
        "    \tCREATE_TIME TIMESTAMP NOT NULL,\n" + //
        "    \tSTART_TIME TIMESTAMP DEFAULT NULL ,\n" + //
        "    \tEND_TIME TIMESTAMP DEFAULT NULL ,\n" + //
        "    \tSTATUS VARCHAR(10) ,\n" + //
        "    \tEXIT_CODE VARCHAR(2500) ,\n" + //
        "    \tEXIT_MESSAGE VARCHAR(2500) ,\n" + //
        "    \tLAST_UPDATED TIMESTAMP,\n" + //
        "    \tconstraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)\n" + //
        "    \treferences BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)\n" + //
        "    )"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_PARAMS  (\n" + //
        "    \tJOB_EXECUTION_ID BIGINT NOT NULL ,\n" + //
        "    \tPARAMETER_NAME VARCHAR(100) NOT NULL ,\n" + //
        "    \tPARAMETER_TYPE VARCHAR(100) NOT NULL ,\n" + //
        "    \tPARAMETER_VALUE VARCHAR(2500) ,\n" + //
        "    \tIDENTIFYING CHAR(1) NOT NULL ,\n" + //
        "    \tconstraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)\n" + //
        "    \treferences BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)\n" + //
        "    )"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION  (\n" + //
        "    \tSTEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,\n" + //
        "    \tVERSION BIGINT NOT NULL,\n" + //
        "    \tSTEP_NAME VARCHAR(100) NOT NULL,\n" + //
        "    \tJOB_EXECUTION_ID BIGINT NOT NULL,\n" + //
        "    \tCREATE_TIME TIMESTAMP NOT NULL,\n" + //
        "    \tSTART_TIME TIMESTAMP DEFAULT NULL ,\n" + //
        "    \tEND_TIME TIMESTAMP DEFAULT NULL ,\n" + //
        "    \tSTATUS VARCHAR(10) ,\n" + //
        "    \tCOMMIT_COUNT BIGINT ,\n" + //
        "    \tREAD_COUNT BIGINT ,\n" + //
        "    \tFILTER_COUNT BIGINT ,\n" + //
        "    \tWRITE_COUNT BIGINT ,\n" + //
        "    \tREAD_SKIP_COUNT BIGINT ,\n" + //
        "    \tWRITE_SKIP_COUNT BIGINT ,\n" + //
        "    \tPROCESS_SKIP_COUNT BIGINT ,\n" + //
        "    \tROLLBACK_COUNT BIGINT ,\n" + //
        "    \tEXIT_CODE VARCHAR(2500) ,\n" + //
        "    \tEXIT_MESSAGE VARCHAR(2500) ,\n" + //
        "    \tLAST_UPDATED TIMESTAMP,\n" + //
        "    \tconstraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)\n" + //
        "    \treferences BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)\n" + //
        "    )"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_CONTEXT  (\n" + //
        "    \tSTEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,\n" + //
        "    \tSHORT_CONTEXT VARCHAR(2500) NOT NULL,\n" + //
        "    \tSERIALIZED_CONTEXT TEXT ,\n" + //
        "    \tconstraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)\n" + //
        "    \treferences BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)\n" + //
        "    )"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_CONTEXT  (\n" + //
        "    \tJOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,\n" + //
        "    \tSHORT_CONTEXT VARCHAR(2500) NOT NULL,\n" + //
        "    \tSERIALIZED_CONTEXT TEXT ,\n" + //
        "    \tconstraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)\n" + //
        "    \treferences BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)\n" + //
        "    )"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE SEQUENCE IF NOT EXISTS BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE SEQUENCE IF NOT EXISTS BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE"
      )
    );

    jdbcTemplate.execute(
      String.format(
        "CREATE SEQUENCE IF NOT EXISTS BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE"
      )
    );
  }
}
