package com.justin.backend.resourceserver.configuration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Autowired
  private CORSCustomer corsCustomer;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    corsCustomer.corsCustomizer(http);
    return http
        .authorizeHttpRequests(customizer -> customizer.anyRequest().authenticated())
        .oauth2ResourceServer(customizer -> customizer
            .jwt(jwtCustomizer -> jwtCustomizer.decoder(JwtDecoders.fromIssuerLocation(issuerUri))))
        .build();
  }
}
