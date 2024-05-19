package com.example.demo.security.component.filter;

import com.example.demo.security.component.provider.JWTProvider;
import com.example.demo.utils.SecurityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

  private final JWTProvider jwtProvider;

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest httpServletRequest,
    @NonNull HttpServletResponse httpServletResponse,
    @NonNull FilterChain filterChain
  ) throws IOException, ServletException {
    try {
      String token = jwtProvider.generateRequestToken(httpServletRequest);

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
      String message = "Invalid Token";

      if (exception instanceof ExpiredJwtException) {
        message = "Expired Token";
      }

      SecurityUtils.sendErrorResponse(
        httpServletRequest,
        httpServletResponse,
        exception,
        message
      );
    }
  }
}
