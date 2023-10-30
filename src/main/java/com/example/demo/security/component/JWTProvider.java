package com.example.demo.security.component;

import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.UserAdapter;
import com.example.demo.security.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTProvider {

  private final UserDetailsServiceImpl userDetailsServiceImpl;

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.access-expire}")
  private long accessExpireTime;

  @Value("${jwt.refresh-expire}")
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
    Claims claims = generateClaims(refreshToken);
    long userId = Long.valueOf(claims.getSubject());

    if (
      securityUserItem.getUserId() == userId &&
      claims.getExpiration().before(new Date(System.currentTimeMillis()))
    ) {
      return createAccessToken(securityUserItem);
    } else {
      throw new ExpiredJwtException(null, claims, "");
    }
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

  private Claims generateClaims(String token)
    throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    return Jwts
      .parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(token)
      .getBody();
  }
}
