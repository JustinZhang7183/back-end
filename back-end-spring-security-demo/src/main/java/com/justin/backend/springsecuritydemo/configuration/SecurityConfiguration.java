package com.justin.backend.springsecuritydemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(customizer ->
        customizer.requestMatchers("/").permitAll()
            .requestMatchers("/user").hasRole("USER")
            .requestMatchers("/admin").hasRole("ADMIN"))
        .formLogin(Customizer.withDefaults()) // login with browser and Form
        .httpBasic(Customizer.withDefaults()); // login with Insomnia/Postman and Basic Auth
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    var admin = User.builder()
        .username("admin")
        .password("{noop}admin")
        .roles("ADMIN", "USER")
        .build();
    var user = User.builder()
        .username("justin")
        .password("{noop}justin")
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(admin, user);
  }
}
