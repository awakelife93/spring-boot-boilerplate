package com.example.demo.common;

import com.example.demo.common.aop.ResponseAdvice;
import com.example.demo.common.exception.handler.ErrorHandler;
import com.example.demo.utils.SwaggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

public class BaseIntegrationControllerItem {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext webApplicationContext;

  @Autowired
  protected ResponseAdvice responseAdvice;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected ErrorHandler errorHandler;

  @MockBean
  protected SwaggerUtils swaggerUtils;

  /**
   * ResponseAdvice Status
   */
  protected int commonStatus = HttpStatus.OK.value();

  /**
   * ResponseAdvice Message
   */
  protected String commonMessage = HttpStatus.OK.name();
}
