package com.example.demo.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class LoggingForInterceptor implements HandlerInterceptor {

  @Override
  public void postHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler,
    ModelAndView modelAndView
  ) throws Exception {
    log.info(
      "{} {} {} - status: {}",
      UUID.randomUUID().toString(),
      request.getRequestURI(),
      handler,
      response.getStatus()
    );
    HandlerInterceptor.super.postHandle(request, response, handler, null);
  }
}
