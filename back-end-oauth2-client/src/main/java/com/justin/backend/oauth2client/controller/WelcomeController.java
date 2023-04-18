package com.justin.backend.oauth2client.controller;

import com.justin.backend.oauth2client.remote.WelcomeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WelcomeController {
  private final WelcomeClient welcomeClient;

  @GetMapping("/")
  public String welcome() {
    String demo = welcomeClient.getDemo();
    return "<h1>Welcome " + demo + " !</h1>";
  }
}
