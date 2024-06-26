package com.example.demo.common.exception.handler;

import com.example.demo.common.exception.AlreadyExistException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UnAuthorizedException;
import com.example.demo.common.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(ExpiredJwtException.class)
  protected ResponseEntity<ErrorResponse> handleExpiredJwtException(
    ExpiredJwtException exception,
    HttpServletRequest httpServletRequest
  ) {
    ErrorResponse response = ErrorResponse.of(
      HttpStatus.UNAUTHORIZED.value(),
      exception.getMessage()
    );

    log.error(
      "handleExpiredJwtException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNotFoundException(
    NotFoundException exception,
    HttpServletRequest httpServletRequest
  ) {
    ErrorResponse response = ErrorResponse.of(
      exception.getCode().value(),
      exception.getMessage()
    );

    log.error(
      "handleNotFoundException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    return ResponseEntity.status(exception.getCode().value()).body(response);
  }

  @ExceptionHandler(UnAuthorizedException.class)
  protected ResponseEntity<ErrorResponse> handleUnAuthorizedException(
    UnAuthorizedException exception,
    HttpServletRequest httpServletRequest
  ) {
    ErrorResponse response = ErrorResponse.of(
      exception.getCode().value(),
      exception.getMessage()
    );

    log.error(
      "handleUnAuthorizedException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    return ResponseEntity.status(exception.getCode().value()).body(response);
  }

  @ExceptionHandler(AlreadyExistException.class)
  protected ResponseEntity<ErrorResponse> handleAlreadyExistException(
    AlreadyExistException exception,
    HttpServletRequest httpServletRequest
  ) {
    ErrorResponse response = ErrorResponse.of(
      exception.getCode().value(),
      exception.getMessage()
    );

    log.error(
      "handleAlreadyExistException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    return ResponseEntity.status(exception.getCode().value()).body(response);
  }

  @ExceptionHandler(BindException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
    BindException exception,
    HttpServletRequest httpServletRequest
  ) {
    BindingResult bindingResult = exception.getBindingResult();
    StringBuilder stringBuilder = new StringBuilder();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      stringBuilder.append(fieldError.getField()).append(":");
      stringBuilder.append(fieldError.getDefaultMessage());
      stringBuilder.append(", ");
    }

    ErrorResponse response = ErrorResponse.of(
      HttpStatus.BAD_REQUEST.value(),
      String.valueOf(stringBuilder),
      exception.getFieldErrors()
    );

    log.error(
      "handleMethodArgumentNotValidException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      String.valueOf(stringBuilder)
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<ErrorResponse> handleAuthenticationException(
    AuthenticationException exception,
    HttpServletRequest httpServletRequest
  ) {
    ErrorResponse response = ErrorResponse.of(
      HttpStatus.UNAUTHORIZED.value(),
      exception.getMessage()
    );

    log.error(
      "handleAuthenticationException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
    NoHandlerFoundException exception,
    HttpServletRequest httpServletRequest
  ) {
    ErrorResponse response = ErrorResponse.of(
      HttpStatus.NOT_FOUND.value(),
      exception.getMessage(),
      exception.getRequestHeaders()
    );

    log.error(
      "handleNoHandlerFoundException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(
    Exception exception,
    HttpServletRequest httpServletRequest
  ) {
    final ErrorResponse response = ErrorResponse.of(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      exception.getMessage()
    );

    log.error(
      "handleException Error - {} {} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }
}
