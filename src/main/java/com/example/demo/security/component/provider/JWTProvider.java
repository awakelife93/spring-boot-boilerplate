package com.example.demo.security.component.provider;

import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.UserAdapter;
import com.example.demo.security.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class JWTProvider {

  private final UserDetailsServiceImpl userDetailsServiceImpl;

  @Value("${auth.jwt.secret}")
  private String secretKey;

  @Value("${auth.jwt.access-expire}")
  private long accessExpireTime;

  @Value("${auth.jwt.refresh-expire}")
  private long refreshExpireTime;

  public String createAccessToken(SecurityUserItem securityUserItem) {
    return createToken(securityUserItem, true);
  }

  public String createRefreshToken(SecurityUserItem securityUserItem) {
    return createToken(securityUserItem, false);
  }

  public String createToken(
    SecurityUserItem securityUserItem,
    boolean isAccessToken
  ) {
    Claims claims = Jwts
      .claims()
      .setSubject(String.valueOf(securityUserItem.getUserId()));
    claims.put("email", securityUserItem.getEmail());
    claims.put("role", securityUserItem.getRole());

    long expireTime = isAccessToken ? accessExpireTime : refreshExpireTime;
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expireTime * 1000);

    return Jwts
      .builder()
      .setClaims(claims)
      .setIssuedAt(now)
      .setExpiration(expiration)
      .signWith(SignatureAlgorithm.HS256, secretKey)
      .compact();
  }

  public String refreshAccessToken(
    SecurityUserItem securityUserItem,
    String refreshToken
  ) {
    generateClaims(refreshToken);
    return createAccessToken(securityUserItem);
  }

  public boolean validateToken(String token) {
    Claims claims = generateClaims(token);
    return !claims.isEmpty();
  }

  public String generateRequestToken(HttpServletRequest request) {
    String requestToken = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (!Objects.isNull(requestToken) && requestToken.startsWith("Bearer")) {
      return requestToken.substring(7);
    }

    return null;
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    Claims claims = generateClaims(token);
    UserAdapter userAdapter = (UserAdapter) userDetailsServiceImpl.loadUserByUsername(
      claims.getSubject()
    );

    return new UsernamePasswordAuthenticationToken(
      userAdapter,
      null,
      userAdapter.getAuthorities()
    );
  }

  private Claims generateClaims(String token) {
    return Jwts
      .parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(token)
      .getBody();
  }
}
