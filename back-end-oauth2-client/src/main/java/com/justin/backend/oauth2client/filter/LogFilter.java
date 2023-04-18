package com.justin.backend.oauth2client.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    LocalDateTime date = LocalDateTime.now();
    log.info("LogFilter: {} - {}:{}{}",date, httpRequest.getLocalAddr(), httpRequest.getLocalPort(), httpRequest.getServletPath());
    Enumeration<String> headers = httpRequest.getHeaderNames();
    while (headers.hasMoreElements()) {
      String headerName = headers.nextElement();
      log.info("\tHeader: {}:{}", headerName, httpRequest.getHeader(headerName));
    }
    log.info("\n\n");
    chain.doFilter(request, response);
  }
}
