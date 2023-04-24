package com.justin.backend.zookeeperdemo.servicediscovery.autoconfig;

import com.justin.backend.zookeeperdemo.servicediscovery.discovery.ServiceDiscovery;
import com.justin.backend.zookeeperdemo.servicediscovery.discovery.ZkServiceDiscovery;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ZkDiscoveryAutoConfiguration {
  @Resource
  private Environment environment;

  @Bean
  public ServiceDiscovery serviceDiscovery() {
    return new ZkServiceDiscovery(environment.getProperty("services-path"), environment.getProperty("zookeeper-url"));
  }
}
