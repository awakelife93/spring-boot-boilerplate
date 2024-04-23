package com.example.demo.security.config;

import com.example.demo.common.config.CorsConfig;
import com.example.demo.security.component.CustomAuthenticationEntryPoint;
import com.example.demo.security.component.JWTAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final CorsConfig corsConfig;
  private final JWTAuthFilter jwtAuthFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  private String[] whiteListAuthEndpoints() {
    String[] endPoints = new String[] { "/auth/signIn", "/auth/signUp" };
    return endPoints;
  }

  private String[] whiteListGetEndpoints() {
    String[] endPoints = new String[] {
      "/users",
      "/posts",
      "/posts/exclude-users",
    };
    return endPoints;
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .httpBasic(httpBasic -> httpBasic.disable())
      .formLogin(formLogin -> formLogin.disable())
      .cors(cors ->
        cors.configurationSource(corsConfig.corsConfigurationSource())
      )
      .authorizeHttpRequests(request ->
        request
          .requestMatchers(whiteListAuthEndpoints())
          .permitAll()
          .requestMatchers(HttpMethod.GET, whiteListGetEndpoints())
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .sessionManagement(httpSecuritySessionManagementConfigurer ->
        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
          SessionCreationPolicy.STATELESS
        )
      )
      .addFilterBefore(
        jwtAuthFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .exceptionHandling(exceptionHandling ->
        exceptionHandling.authenticationEntryPoint(
          customAuthenticationEntryPoint
        )
      )
      .build();
  }
}
