package com.justin.backend.springsecuritydemo.configuration;

import com.justin.backend.springsecuritydemo.configuration.providers.CustomNeedPasswordAuthenticationProvider;
import com.justin.backend.springsecuritydemo.configuration.providers.CustomNoNeedPasswordAuthenticationProvider;
import com.justin.backend.springsecuritydemo.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * security config class.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
  private final MyUserDetailsService userDetailsService;

  private final AuthenticationEventPublisher publisher;

  private final PasswordEncoder passwordEncoder;

  /**
   * custom security filter chain.
   */
  @Bean
  public SecurityFilterChain securityFilterChainForUser(HttpSecurity http) throws Exception {
    List<AuthenticationProvider> list = new ArrayList<>();
    list.add(new CustomNeedPasswordAuthenticationProvider(passwordEncoder, userDetailsService));
//    list.add(new CustomNoNeedPasswordAuthenticationProvider());
    ProviderManager providerManager = new ProviderManager(list);
    providerManager.setAuthenticationEventPublisher(publisher);

    http.authorizeHttpRequests(authConfig ->
            authConfig.requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/authority").permitAll()
                .anyRequest().authenticated())
        .csrf().disable() // just set for post request temporarily
        .userDetailsService(userDetailsService)
        .addFilterBefore(new PreLoginSecurityFilter(providerManager), UsernamePasswordAuthenticationFilter.class)
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public ApplicationListener<AuthenticationSuccessEvent> successEvent() {
    return event -> log.info("Success Login " + event.getAuthentication().getClass().getName() + " - " + event.getAuthentication().getName());
  }

  @Bean
  public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEvent() {
    return event -> log.info("Bad Credentials Login " + event.getAuthentication().getClass().getName() + " - " + event.getAuthentication().getName());
  }
}
