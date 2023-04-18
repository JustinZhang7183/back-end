package com.justin.backend.oauth2client.remote;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://localhost:9000")
public interface WelcomeClient {
  @GetExchange("/demo")
  String getDemo();
}
