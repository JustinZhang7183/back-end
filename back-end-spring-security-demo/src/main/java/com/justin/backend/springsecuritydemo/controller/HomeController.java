package com.justin.backend.springsecuritydemo.controller;

import com.justin.backend.springsecuritydemo.configuration.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Welcome home!";
    }

    @GetMapping("/user")
    public String user(Authentication authentication) {
        return "Welcome " + authentication.getName() + "!";
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        return "Welcome " + authentication.getName() + "! Your authorities: " + authentication.getAuthorities() +
                "<br>" + user.getFullName() +
                "<br>" + user.getEmailAddress() +
                "<br>" + user.getBirthDate();
    }
}
