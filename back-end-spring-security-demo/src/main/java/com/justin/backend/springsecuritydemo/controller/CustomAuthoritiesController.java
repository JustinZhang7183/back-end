package com.justin.backend.springsecuritydemo.controller;

import com.justin.backend.springsecuritydemo.model.CustomAuthorities;
import com.justin.backend.springsecuritydemo.service.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomAuthoritiesController {
  private final MyUserDetailsService myUserDetailsService;

  public CustomAuthoritiesController(MyUserDetailsService myUserDetailsService) {
    this.myUserDetailsService = myUserDetailsService;
  }

  @PostMapping("/authority")
  public ResponseEntity<CustomAuthorities> createAuthority(@RequestBody CustomAuthorities authority) {
    CustomAuthorities savedAuthority = myUserDetailsService.createAuthority(authority);
    return ResponseEntity.ok(savedAuthority);
  }

}
