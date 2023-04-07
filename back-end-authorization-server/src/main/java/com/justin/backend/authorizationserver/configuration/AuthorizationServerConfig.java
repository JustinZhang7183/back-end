package com.justin.backend.authorizationserver.configuration;

import com.justin.backend.authorizationserver.utils.JwtUtil;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;
import java.util.UUID;

@Configuration
@AllArgsConstructor
public class AuthorizationServerConfig {
  private final CORSCustomer corsCustomer;

  /**
   * some default configuration. set the handler for exception.
   *
   * @param http provided by spring context
   * @return SecurityFilterChain
   * @throws Exception exception
   */
  @Bean
  @Order(1)
  public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
    http.exceptionHandling(e -> e
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
    corsCustomer.corsCustomizer(http);
    return http.build();
  }

  /**
   * client config
   *
   * @return RegisteredClientRepository
   */
  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("client")
        .clientSecret("secret")
        .scope(OidcScopes.OPENID) // TODO: What's difference?
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://127.0.0.1:3000/authorized") // TODO: why can't use localhost
        .clientSettings(clientSettings())
        .build();
    return new InMemoryRegisteredClientRepository(registeredClient);
  }

  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build(); // TODO: Is it needed?
  }

  @Bean
  public TokenSettings tokenSettings() {
    return TokenSettings.builder()
        .accessTokenTimeToLive(Duration.ofHours(1)) // TODO: should be the same?
        .refreshTokenTimeToLive(Duration.ofHours(1))
        .build();
  }

  @Bean
  public ClientSettings clientSettings() {
    return ClientSettings.builder()
        .requireAuthorizationConsent(true) // TODO: difference?
        .requireProofKey(true) // need PKCE
        .build();
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    RSAKey rsaKey = JwtUtil.generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

}
