package com.justin.backend.tool.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @RequestMapping("/health")
  public String health(HttpServletRequest request) {
    String remoteHost = request.getRemoteHost();
    return remoteHost;
  }
}
