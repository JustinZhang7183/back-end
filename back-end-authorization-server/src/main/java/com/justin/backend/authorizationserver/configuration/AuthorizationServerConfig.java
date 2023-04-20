package com.justin.backend.authorizationserver.configuration;

import com.justin.backend.authorizationserver.utils.JwtUtil;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class AuthorizationServerConfig {
  private final CORSCustomer corsCustomer;

  private final PasswordEncoder passwordEncoder;

  /**
   * some default configuration. set the handler for exception.
   *
   * @param http provided by spring context
   * @return SecurityFilterChain
   * @throws Exception exception
   */
  @Bean
  public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .oidc(Customizer.withDefaults());
    http.exceptionHandling(e -> e
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
    corsCustomer.corsCustomizer(http);
    return http.build();
  }

  /**
   * authorization server settings, like some endpoints.
   *
   * @return AuthorizationServerSettings
   */
  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
  }

  @Bean
  OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
    return context -> {
      Authentication principal = context.getPrincipal();
      if (context.getTokenType().getValue().equals("id_token")) {
        context.getClaims().claim("Test", "Test Id Token");
      }
      if (context.getTokenType().getValue().equals("access_token")) {
        context.getClaims().claim("Test", "Test Access Token");
        Set<String> authorities = principal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        context.getClaims().claim("authorities", authorities)
            .claim("user", principal.getName());
      }
    };
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    RSAKey rsaKey = JwtUtil.generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

}
