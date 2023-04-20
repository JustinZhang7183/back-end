package com.justin.backend.authorizationserver.service;

import com.justin.backend.authorizationserver.entities.CustomUsers;
import com.justin.backend.authorizationserver.repository.CustomUsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
  private final CustomUsersRepository usersRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<CustomUsers> usersOptional = usersRepository.findByUsername(username);
    CustomUsers user = usersOptional.get();
    return new User(user.getUsername(), user.getPassword(), user.getEnabled(), user.getAccountNonExpired(),
        user.getCredentialsNonExpired(), user.getAccountNonLocked(),
        user.getCustomAuthorities().stream().map(authority ->
            new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toSet()));
//    return user.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
