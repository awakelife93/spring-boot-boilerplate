package com.example.demo.common.exception.handler;

import com.example.demo.common.exception.AlreadyExistException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UnAuthorizedException;
import com.example.demo.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNotFoundException(
    NotFoundException exception
  ) {
    ErrorResponse response = ErrorResponse.of(
      exception.getCode().value(),
      exception.getMessage()
    );

    log.error("handleNotFoundException Error", exception);
    return ResponseEntity.status(exception.getCode().value()).body(response);
  }

  @ExceptionHandler(UnAuthorizedException.class)
  protected ResponseEntity<ErrorResponse> handleUnAuthorizedException(
    UnAuthorizedException exception
  ) {
    ErrorResponse response = ErrorResponse.of(
      exception.getCode().value(),
      exception.getMessage()
    );

    log.error("handleUnAuthorizedException Error", exception);
    return ResponseEntity.status(exception.getCode().value()).body(response);
  }

  @ExceptionHandler(AlreadyExistException.class)
  protected ResponseEntity<ErrorResponse> handleAlreadyExistException(
    AlreadyExistException exception
  ) {
    ErrorResponse response = ErrorResponse.of(
      exception.getCode().value(),
      exception.getMessage()
    );

    log.error("handleAlreadyExistException Error", exception);
    return ResponseEntity.status(exception.getCode().value()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException exception
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

    log.error("handleMethodArgumentNotValidException Error", exception);
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
    NoHandlerFoundException exception
  ) {
    ErrorResponse response = ErrorResponse.of(
      HttpStatus.NOT_FOUND.value(),
      exception.getMessage(),
      exception.getRequestHeaders()
    );

    log.error("handleNoHandlerFoundException Error", exception);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
    final ErrorResponse response = ErrorResponse.of(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      exception.getMessage()
    );

    log.error("handleException Error", exception);
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }
}
