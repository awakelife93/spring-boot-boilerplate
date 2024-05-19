package com.example.demo.security.component.filter;

import com.example.demo.security.component.provider.AuthProvider;
import com.example.demo.security.exception.APIKeyNotFoundException;
import com.example.demo.utils.SecurityUtils;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class APIKeyAuthFilter extends OncePerRequestFilter {

  private final AuthProvider authProvider;

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest httpServletRequest,
    @NonNull HttpServletResponse httpServletResponse,
    @NonNull FilterChain filterChain
  ) throws IOException, ServletException {
    try {
      if (!authProvider.validateApiKey(httpServletRequest)) {
        throw new APIKeyNotFoundException(httpServletRequest.getRequestURI());
      }

      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } catch (APIKeyNotFoundException exception) {
      SecurityUtils.sendErrorResponse(
        httpServletRequest,
        httpServletResponse,
        exception,
        "UnAuthorized API Key"
      );
    }
  }
}
