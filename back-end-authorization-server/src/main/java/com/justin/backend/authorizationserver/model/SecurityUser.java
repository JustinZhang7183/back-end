package com.justin.backend.authorizationserver.model;

import com.justin.backend.authorizationserver.entities.CustomUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {
  private final CustomUsers user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getAuthorities().stream()
        .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return user.getAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.getAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return user.getCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return user.getEnabled();
  }
}
