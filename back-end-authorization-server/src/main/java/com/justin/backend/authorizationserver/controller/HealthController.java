package com.justin.backend.authorizationserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello!";
    }
}
