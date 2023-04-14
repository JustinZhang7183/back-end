package com.justin.backend.springsecuritydemo.controller;

import com.justin.backend.springsecuritydemo.model.CustomUsers;
import com.justin.backend.springsecuritydemo.service.MyUserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomUserController {
  private final MyUserDetailsService userDetailsService;

  public CustomUserController(MyUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostMapping("/user")
  public void createUser(@RequestBody CustomUsers user) {
    userDetailsService.createUser(user);
  }
}
