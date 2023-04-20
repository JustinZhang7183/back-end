package com.justin.backend.authorizationserver.model;

import com.justin.backend.authorizationserver.entities.CustomUsers;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

// TODO: if I use this object, there will be a issue of jackson. (allow list for deserialization; PersistentSet;)
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements UserDetails {
  private CustomUsers user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return user.getCustomAuthorities().stream()
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
