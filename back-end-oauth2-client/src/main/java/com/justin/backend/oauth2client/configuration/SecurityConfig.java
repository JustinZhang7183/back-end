package com.justin.backend.oauth2client.configuration;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class SecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
    String baseUri = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
    DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, baseUri);
    resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

    http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .oauth2Login(oauth2Login -> {
          oauth2Login.loginPage("/oauth2/authorization/myoauth2")
              .authorizationEndpoint()
              .authorizationRequestResolver(resolver);
          oauth2Login.userInfoEndpoint(userinfo -> userinfo.oidcUserService(oidcUserService()));
        })
        .oauth2Client(Customizer.withDefaults());
    return http.build();
  }

  private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
    final OidcUserService delegate = new OidcUserService();
    return (userRequest -> {
      OidcUser oidcUser = delegate.loadUser(userRequest);
      OAuth2AccessToken accessToken = userRequest.getAccessToken();
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      try {
        JWT jwt = JWTParser.parse(accessToken.getTokenValue());
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        Collection<String> userAuthorities = claimsSet.getStringListClaim("authorities");
        mappedAuthorities.addAll(userAuthorities.stream().map(SimpleGrantedAuthority::new).toList());
      } catch (ParseException e) {
        log.error("Error OAuth2UserService: " + e.getMessage());
      }
      oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
      return oidcUser;
    });
  }
}
