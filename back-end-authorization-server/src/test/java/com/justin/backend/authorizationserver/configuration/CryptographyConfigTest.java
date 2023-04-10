package com.justin.backend.authorizationserver.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class CryptographyConfigTest {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void test() {
    String nacos = passwordEncoder.encode("nacos");
    System.out.println(nacos);
  }
}
