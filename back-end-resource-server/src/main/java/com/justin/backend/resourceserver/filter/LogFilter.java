package com.justin.backend.resourceserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter implements WebFilter {
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    LocalDateTime date = LocalDateTime.now();
    log.info("LogFilter: {} - {}:{}{}",date, request.getLocalAddress(), request.getLocalAddress(), request.getPath());
    HttpHeaders headers = request.getHeaders();
    headers.forEach((header, headerList) -> {
      log.info("\tHeader: {}:{}", header, headerList.get(0));
    });
    log.info("\n\n");
    return chain.filter(exchange);
  }
}
