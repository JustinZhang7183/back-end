package com.justin.backend.authorizationserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justin.backend.authorizationserver.entities.Client;
import com.justin.backend.authorizationserver.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
  private final ClientRepository clientRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public JpaRegisteredClientRepository(ClientRepository clientRepository) {
    Assert.notNull(clientRepository, "clientRepository can not be null");
    this.clientRepository = clientRepository;
    ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
    List<Module> modules = SecurityJackson2Modules.getModules(classLoader);
    objectMapper.registerModules(modules);
    objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
  }

  @Override
  public void save(RegisteredClient registeredClient) {
    Assert.notNull(registeredClient, "registeredClient can not be null");
    clientRepository.save(toEntity(registeredClient));
  }

  @Override
  public RegisteredClient findById(String id) {
    Assert.hasText(id, "clientId cannot be empty");
    return clientRepository.findById(id).map(this::toObject).orElse(null);
  }

  @Override
  public RegisteredClient findByClientId(String clientId) {
    Assert.hasText(clientId, "clientId can not be empty");
    return clientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
  }

  private RegisteredClient toObject(Client client) {
    Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());
    Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());
    Set<String> redirectUris = StringUtils.commaDelimitedListToSet(client.getRedirectUris());
    Set<String> clientScopes = StringUtils.commaDelimitedListToSet(client.getScopes());
    RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
        .clientId(client.getClientId())
        .clientIdIssuedAt(client.getClientIdIssuedAt())
        .clientSecret(client.getClientSecret())
        .clientSecretExpiresAt(client.getClientSecretExpiresAt())
        .clientName(client.getClientName())
        .clientAuthenticationMethods(authenticationMethods ->
            clientAuthenticationMethods.forEach(authenticationMethod ->
                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
        .authorizationGrantTypes(grantTypes ->
            authorizationGrantTypes.forEach(grantType ->
                grantTypes.add(resolveAuthorizationGrantType(grantType))))
        .redirectUris(uris -> uris.addAll(redirectUris))
        .scopes(scopes -> scopes.addAll(clientScopes));
    Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
    builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());
    Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
    builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());
    return builder.build();
  }

  private Client toEntity(RegisteredClient registeredClient) {
    List<String> clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
    registeredClient.getClientAuthenticationMethods().forEach(clientAuthenticationMethod ->
        clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));
    List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
    registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
        authorizationGrantTypes.add(authorizationGrantType.getValue()));
    Client entity = new Client();
    entity.setId(registeredClient.getId())
        .setClientId(registeredClient.getClientId())
        .setClientIdIssuedAt(registeredClient.getClientIdIssuedAt())
        .setClientSecret(registeredClient.getClientSecret())
        .setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt())
        .setClientName(registeredClient.getClientName())
        .setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods))
        .setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes))
        .setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()))
        .setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()))
        .setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()))
        .setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));
    return entity;
  }

  private String writeMap(Map<String, Object> data) {
    try {
      return objectMapper.writeValueAsString(data);
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  private Map<String, Object> parseMap(String data) {
    try {
      return objectMapper.readValue(data, new TypeReference<>() {
      });
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.AUTHORIZATION_CODE;
    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.CLIENT_CREDENTIALS;
    } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.REFRESH_TOKEN;
    }
    return new AuthorizationGrantType(authorizationGrantType);
  }

  private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
    if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
    } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_POST;
    } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.NONE;
    }
    return new ClientAuthenticationMethod(clientAuthenticationMethod);
  }
}
