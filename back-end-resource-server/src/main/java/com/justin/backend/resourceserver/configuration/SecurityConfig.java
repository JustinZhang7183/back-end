package com.justin.backend.resourceserver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
  @Autowired
  private CORSCustomer corsCustomer;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
    corsCustomer.corsCustomizer(http);
    http.authorizeExchange(exchange -> exchange.anyExchange().authenticated())
        .oauth2ResourceServer(customizer -> customizer
            .jwt(jwtCustomizer -> jwtCustomizer.jwtDecoder(jwtDecoder())));
    return http.build();
  }

  @Bean
  public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
    grantedAuthoritiesConverter.setAuthorityPrefix("");

    ReactiveJwtAuthenticationConverter reactiveJwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
    reactiveJwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new ReactiveJwtGrantedAuthoritiesConverterAdapter(grantedAuthoritiesConverter));

    return reactiveJwtAuthenticationConverter;
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
    return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
  }
}
