package com.justin.backend.authorizationserver.service;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justin.backend.authorizationserver.entities.Client;
import com.justin.backend.authorizationserver.repository.ClientRepository;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

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
  }

  @Override
  public RegisteredClient findById(String id) {
    Assert.hasText(id, "clientId cannot be empty");
    return clientRepository.findById(id).map(this::toObject).orElse(null);
  }

  @Override
  public RegisteredClient findByClientId(String clientId) {
    return null;
  }

  private RegisteredClient toObject(Client client) {
    return null;
  }
}
