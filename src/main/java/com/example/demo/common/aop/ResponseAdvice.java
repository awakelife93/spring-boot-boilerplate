package com.example.demo.common.aop;

import com.example.demo.common.response.ErrorResponse;
import com.example.demo.common.response.OkResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
    MethodParameter returnType,
    Class<? extends HttpMessageConverter<?>> converterType
  ) {
    return MappingJackson2HttpMessageConverter.class.isAssignableFrom(
        converterType
      );
  }

  @Override
  public Object beforeBodyWrite(
    @Nullable Object body,
    MethodParameter returnType,
    MediaType selectedContentType,
    Class<? extends HttpMessageConverter<?>> selectedConverterType,
    ServerHttpRequest request,
    ServerHttpResponse response
  ) {
    if (body instanceof ErrorResponse) {
      return body;
    }

    return OkResponse.of(HttpStatus.OK.value(), HttpStatus.OK.name(), body);
  }
}
