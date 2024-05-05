package com.example.demo.common;

import com.example.demo.utils.SwaggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

public class BaseIntegrationControllerItem {

  @Autowired
  protected WebApplicationContext webApplicationContext;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected SwaggerUtils swaggerUtils;

  protected MockMvc mockMvc;

  /**
   * ResponseAdvice Status
   */
  protected int commonStatus = HttpStatus.OK.value();

  /**
   * ResponseAdvice Message
   */
  protected String commonMessage = HttpStatus.OK.name();
}
