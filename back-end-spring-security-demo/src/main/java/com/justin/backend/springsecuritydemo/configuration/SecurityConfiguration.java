package com.justin.backend.springsecuritydemo.configuration;

import com.justin.backend.springsecuritydemo.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * security config class.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
  private final MyUserDetailsService userDetailsService;

  private final PasswordEncoder passwordEncoder;

  /**
   * custom security filter chain.
   */
  @Bean
  public SecurityFilterChain securityFilterChainForUser(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authConfig ->
            authConfig.requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/authority").permitAll()
                .anyRequest().authenticated())
        .csrf().disable() // just set for post request temporarily
        .apply(new CustomLoginConfigurer(userDetailsService, passwordEncoder))
        .and()
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public ApplicationListener<AuthenticationSuccessEvent> successEvent() {
    return event -> log.info("Success Login " + event.getAuthentication().getClass().getName() + " - " + event.getAuthentication().getName());
  }

  @Bean
  public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEvent() {
    return event -> log.info("Bad Credentials Login " + event.getAuthentication().getClass().getName() + " - " + event.getAuthentication().getName());
  }
}
