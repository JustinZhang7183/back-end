package com.justin.backend.springsecuritydemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@SpringBootApplication
public class SpringSecurityDemoRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoRunner.class, args);
    }

//    @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
        return args -> {
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
            jdbcUserDetailsManager.createUser(admin);
            jdbcUserDetailsManager.createUser(user);
        };
    }
}
