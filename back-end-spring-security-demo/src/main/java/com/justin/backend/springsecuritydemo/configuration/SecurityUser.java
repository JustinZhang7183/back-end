package com.justin.backend.springsecuritydemo.configuration;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Collection;

@Data
public class SecurityUser extends User {
  private String firstName;

  private String lastName;

  private String fullName;

  private String emailAddress;

  private LocalDate birthDate;

  public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  public SecurityUser(String username,
                      String password,
                      boolean enabled,
                      boolean accountNonExpired,
                      boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities,
                      String firstName,
                      String lastName,
                      String emailAddress,
                      LocalDate birthDate) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.firstName = firstName;
    this.lastName = lastName;
    this.fullName = firstName + " " + lastName;
    this.emailAddress = emailAddress;
    this.birthDate = birthDate;
  }
}
