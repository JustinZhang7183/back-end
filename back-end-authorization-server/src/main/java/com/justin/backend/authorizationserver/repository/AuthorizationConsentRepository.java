package com.justin.backend.authorizationserver.repository;

import java.util.Optional;

import com.justin.backend.authorizationserver.entities.AuthorizationConsent;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorizationConsentRepository extends JpaRepository<AuthorizationConsent, AuthorizationConsent.AuthorizationConsentId> {

  Optional<AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
  void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
