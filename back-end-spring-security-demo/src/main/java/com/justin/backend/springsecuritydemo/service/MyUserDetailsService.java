package com.justin.backend.springsecuritydemo.service;

import com.justin.backend.springsecuritydemo.configuration.SecurityUser;
import com.justin.backend.springsecuritydemo.model.CustomAuthorities;
import com.justin.backend.springsecuritydemo.model.CustomUsers;
import com.justin.backend.springsecuritydemo.repository.CustomAuthoritiesRepository;
import com.justin.backend.springsecuritydemo.repository.CustomUsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
  private final CustomUsersRepository usersRepository;

  private final CustomAuthoritiesRepository authoritiesRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<CustomUsers> foundUser = usersRepository.findByUsername(username);
    if (!foundUser.isPresent()) {
      log.error("Could not find user with that username: {}", username);
      throw new UsernameNotFoundException("Invalid credentials!");
    }
    CustomUsers user = foundUser.get();
    if (user == null || !user.getUsername().equals(username)) {
      log.error("Could not find user with that username: {}", username);
      throw new UsernameNotFoundException("Invalid credentials!");
    }
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    user.getAuthorities().stream().forEach(authority ->
        grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority())));

    return new SecurityUser(user.getUsername(),
        user.getPassword(),
        true,
        true,
        true,
        true,
        grantedAuthorities,
        user.getFirstName(),
        user.getLastName(),
        user.getEmailAddress(),
        user.getBirthDate());
  }

  public void createUser(CustomUsers user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    usersRepository.save(user);
  }

  public CustomAuthorities createAuthority(CustomAuthorities authority) {
    return authoritiesRepository.save(authority);
  }
}
