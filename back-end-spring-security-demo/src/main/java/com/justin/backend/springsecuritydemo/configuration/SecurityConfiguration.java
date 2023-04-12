package com.justin.backend.springsecuritydemo.configuration;

import com.justin.backend.springsecuritydemo.test.orderannotation.OrderTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.List;

/**
 * security config class.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  @Autowired
  private List<OrderTest> orderTests;

  /**
   * custom security filter chain.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(customizer ->
        customizer.requestMatchers("/").permitAll()
            .requestMatchers("/user").hasRole("USER")
            .requestMatchers("/admin").hasRole("ADMIN")
            .anyRequest().authenticated())
        .csrf().disable() // for h2-console
        .headers().frameOptions().disable() // for h2-console
        .and()
        .formLogin(Customizer.withDefaults()) // login with browser and Form
        .httpBasic(Customizer.withDefaults()); // login with Insomnia/Postman and Basic Auth
    return http.build();
  }

  /**
   * utilize the embedded data source.
   */
//  @Bean
  public DataSource embeddedH2DataSource() {
    orderTests.forEach(orderTest -> System.out.println(orderTest.getBeanName())); // for @Order test
    return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build();
  }

  /**
   * custom data source, need provide the schema.sql
   */
//  @Bean
  public DataSource customMemoryH2DataSource() {
    return DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:testdb")
            .username("sa")
            .password("")
            .build();
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

  /**
   * in memory user detail config
   * ConditionalOnMissingBean is base on the order of bean definition in the class.
   */
//  @Bean
  public UserDetailsService userDetailsService() {
    var admin = User.builder()
            .username("admin")
            .password("{noop}admin")
            .roles("ADMIN", "USER")
            .build();
    var user = User.builder()
            .username("justin")
            .password("{noop}justin")
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(admin, user);
  }
}
