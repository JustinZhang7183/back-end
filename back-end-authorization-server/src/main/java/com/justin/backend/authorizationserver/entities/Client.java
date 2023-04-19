package com.justin.backend.authorizationserver.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "client")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
  @Id
  private String id;
  private String clientId;
  private String clientName;
  private String clientSecret;
  private Instant clientIdIssuedAt;
  private Instant clientSecretExpiresAt;
  @Column(length = 1000)
  private String clientAuthenticationMethods;
  @Column(length = 1000)
  private String authorizationGrantTypes;
  @Column(length = 1000)
  private String redirectUris;
  @Column(length = 1000)
  private String scopes;
  @Column(length = 2000)
  private String clientSettings;
  @Column(length = 2000)
  private String tokenSettings;

}
