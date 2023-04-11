package com.justin.backend.springsecuritydemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "Welcome home!";
    }

    @RequestMapping("/user")
    public String user(Authentication authentication) {
        return "Welcome " + authentication.getName() + "!";
    }

    @RequestMapping("/admin")
    public String admin(Authentication authentication) {
        return "Welcome " + authentication.getName() + "!";
    }
}
