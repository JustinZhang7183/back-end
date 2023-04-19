package com.justin.backend.oauth2client.controller;

import com.justin.backend.oauth2client.remote.WelcomeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WelcomeController {
  private final WelcomeClient welcomeClient;

  private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

  @GetMapping("/")
  public String welcome(Authentication authentication) {
    String demo = welcomeClient.getDemo();
    return "<h1>Welcome " + demo + " !</h1>" + "<br>" + authentication.getAuthorities();
  }

  @GetMapping("/token")
  public String token(Authentication authentication) {
    OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
    OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService
        .loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
            oAuth2AuthenticationToken.getName());
    String jwtAccessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    String jwtRefreshToken = oAuth2AuthorizedClient.getRefreshToken().getTokenValue();
    return "JWT Access Token: " + jwtAccessToken + "<br>" + "JWT Refresh Token: " + jwtRefreshToken;
  }

  @GetMapping("/idtoken")
  public String idtoken(@AuthenticationPrincipal OidcUser oidcUser) {
    return "Id Token: " + oidcUser.getIdToken().getTokenValue();
  }
}
