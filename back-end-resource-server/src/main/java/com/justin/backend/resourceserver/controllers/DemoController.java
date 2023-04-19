package com.justin.backend.resourceserver.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @GetMapping("/demo")
  public String demo(Authentication authentication) {
    return "Demo" + "<br>" + authentication.getName() + " - " + authentication.getAuthorities();
  }
}
