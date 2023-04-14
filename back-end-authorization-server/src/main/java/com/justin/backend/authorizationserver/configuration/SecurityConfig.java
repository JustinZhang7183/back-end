package com.justin.backend.authorizationserver.configuration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
  private final CORSCustomer corsCustomer;

  /**
   * designate login page, and request authentication.
   * This is the new way to config. Because WebConfigurationAdaptor is deprecated.
   *
   * @param http provided by spring context
   * @return SecurityFilterChain
   * @throws Exception exception
   */
  @Bean
  @Order(2)
  public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
    corsCustomer.corsCustomizer(http);
    http
        .formLogin()
        .and()
        .authorizeHttpRequests().anyRequest().authenticated();
    return http.build();
  }

  /**
   * custom user detail service.
   *
   * @return UserDetailsService
   */
  @Bean
  public UserDetailsService userDetailsService() {
    var user = User.withUsername("justin")
        .password("justin")
        .authorities("test")
        .build();
    return new InMemoryUserDetailsManager(user);
  }

  /**
   * custom password encoder.
   *
   * @return PasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }


}
