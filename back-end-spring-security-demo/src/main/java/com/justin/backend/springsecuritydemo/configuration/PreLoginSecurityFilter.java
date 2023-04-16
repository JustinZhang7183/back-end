package com.justin.backend.springsecuritydemo.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class PreLoginSecurityFilter extends OncePerRequestFilter {
  private final String HEADER_USER_NAME = "x-filter-username";

  private final String HEADER_PASSWORD = "x-filter-password";

  private final AuthenticationManager authenticationManager;

  public PreLoginSecurityFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("before pre login filter");
    if (request.getHeader(HEADER_USER_NAME) == null || request.getHeader(HEADER_PASSWORD) == null) {
      log.info("no needed header");
      filterChain.doFilter(request, response);
      return;
    }
    var username = request.getHeader(HEADER_USER_NAME);
    var password = request.getHeader(HEADER_PASSWORD);
    CustomAuthentication authRequest = CustomAuthentication.unauthenticated(username, password);
    try {
      Authentication authenticate = authenticationManager.authenticate(authRequest);
      var context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authenticate);
      SecurityContextHolder.setContext(context);
      filterChain.doFilter(request, response);
    } catch (AuthenticationException e) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.getWriter().println("your password is wrong!");
    }
    log.info("after pre login filter");
  }
}
