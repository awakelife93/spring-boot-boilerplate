package com.example.demo.security.component;

import com.example.demo.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

  private final JWTProvider jwtProvider;

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest httpServletRequest,
    @NonNull HttpServletResponse httpServletResponse,
    @NonNull FilterChain filterChain
  ) throws IOException, ServletException {
    String token = jwtProvider.generateRequestToken(httpServletRequest);

    try {
      if (!Objects.isNull(token) && jwtProvider.validateToken(token)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtProvider.getAuthentication(
          token
        );
        SecurityContextHolder
          .getContext()
          .setAuthentication(usernamePasswordAuthenticationToken);
      } else {
        SecurityContextHolder.clearContext();
      }

      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } catch (Exception exception) {
      SecurityContextHolder.clearContext();

      if (exception instanceof ExpiredJwtException) {
        sendErrorResponse(httpServletResponse, exception, "EXPIRED_TOKEN");
      } else {
        sendErrorResponse(httpServletResponse, exception, "INVALID_TOKEN");
      }
    }
  }

  private void sendErrorResponse(
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

    try {
      httpServletResponse
        .getWriter()
        .write(objectMapper.writeValueAsString(errorResponse));
    } catch (IOException thisException) {
      thisException.printStackTrace();
    }
  }
}
