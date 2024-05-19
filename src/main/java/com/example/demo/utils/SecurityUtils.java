package com.example.demo.utils;

import com.example.demo.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Slf4j
public class SecurityUtils {

  public static void sendErrorResponse(
    HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse,
    Exception exception,
    String message
  ) {
    ObjectMapper objectMapper = new ObjectMapper();
    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ErrorResponse errorResponse = ErrorResponse.of(
      HttpStatus.UNAUTHORIZED.value(),
      message,
      exception.getMessage()
    );

    log.error(
      "Security Filter sendErrorResponse - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );

    try {
      httpServletResponse
        .getWriter()
        .write(objectMapper.writeValueAsString(errorResponse));
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }
}
