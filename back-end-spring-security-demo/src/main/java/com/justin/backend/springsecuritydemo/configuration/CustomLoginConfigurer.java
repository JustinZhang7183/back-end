package com.justin.backend.springsecuritydemo.configuration;

import com.justin.backend.springsecuritydemo.configuration.providers.CustomNeedPasswordAuthenticationProvider;
import com.justin.backend.springsecuritydemo.configuration.providers.CustomNoNeedPasswordAuthenticationProvider;
import com.justin.backend.springsecuritydemo.service.MyUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomLoginConfigurer extends AbstractHttpConfigurer<CustomLoginConfigurer, HttpSecurity> {
  private final MyUserDetailsService userDetailsService;

  private final PasswordEncoder passwordEncoder;

  public CustomLoginConfigurer(MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void init(HttpSecurity builder) throws Exception {
    builder.authenticationProvider(new CustomNeedPasswordAuthenticationProvider(passwordEncoder, userDetailsService))
        .authenticationProvider(new CustomNoNeedPasswordAuthenticationProvider());
  }

  @Override
  public void configure(HttpSecurity builder) throws Exception {
    AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
    builder.addFilterBefore(new PreLoginSecurityFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
  }
}
