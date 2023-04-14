package com.justin.backend.springsecuritydemo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * security config class.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  /**
   * custom security filter chain for user path.
   */
  @Bean
  @Order(100)
  public SecurityFilterChain securityFilterChainForUser(HttpSecurity http) throws Exception {
    http.securityMatcher("/user")
        .authorizeHttpRequests(authConfig ->
            authConfig.requestMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults());
    return http.build();
  }

  /**
   * custom security filter chain for admin path.
   */
  @Bean
  @Order(101)
  public SecurityFilterChain securityFilterChainForAdmin(HttpSecurity http) throws Exception {
    http.securityMatcher("/admin")
        .authorizeHttpRequests(authConfig ->
            authConfig.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults());
    return http.build();
  }

  /**
   * custom security filter chain for home path.
   */
  @Bean
  @Order(102)
  public SecurityFilterChain securityFilterChainForHome(HttpSecurity http) throws Exception {
    http.securityMatcher("/")
        .authorizeHttpRequests(authConfig ->
            authConfig.anyRequest().permitAll())
        .formLogin(Customizer.withDefaults());
    return http.build();
  }

  /**
   * custom security filter chain for other path.
   */
  @Bean
  @Order(103)
  public SecurityFilterChain securityFilterChainForOther(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authConfig ->
            authConfig.anyRequest().denyAll())
        .formLogin(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public DataSource customMysqlDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }
}
