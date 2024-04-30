package com.example.demo.utils;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class BatchUtils {

  public void setParameter(ChunkContext chunkContext, String key, Object data) {
    chunkContext
      .getStepContext()
      .getStepExecution()
      .getJobExecution()
      .getExecutionContext()
      .put(key, data);
  }

  public Object getParameter(ChunkContext chunkContext, String key) {
    return chunkContext
      .getStepContext()
      .getStepExecution()
      .getJobExecution()
      .getExecutionContext()
      .get(key);
  }
}
