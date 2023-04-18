package com.justin.backend.authorizationserver.service;

import com.justin.backend.authorizationserver.model.CustomUsers;
import com.justin.backend.authorizationserver.model.SecurityUser;
import com.justin.backend.authorizationserver.repository.CustomUsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
  private final CustomUsersRepository usersRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<CustomUsers> user = usersRepository.findByUsername(username);
    return user.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
