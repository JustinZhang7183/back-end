package com.justin.backend.springsecuritydemo.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Order determine the aspect order.
 */
@Order(10)
@Aspect
@Component
public class OrderTestAspectOne {
    @Pointcut("execution(* com.justin.backend.springsecuritydemo.controller.HomeController.user(..))")
    public void user() {
    }

    @Before("user()")
    public void beforeUser(JoinPoint joinPoint) {
        System.out.println("aspect one before: " + joinPoint.getSignature().getName());
    }

    @After("user()")
    public void afterUser(JoinPoint joinPoint) {
        System.out.println("aspect one after: " + joinPoint.getSignature().getName());
    }
}
