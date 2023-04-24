package com.justin.backend.springcloudzookeeper.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/demo")
public class DemoController {
  @Resource
  private Environment environment;

  @Value("${ip}")
  private String ip;

  @Value("${mysql-password}")
  private String mysqlPassword;

  @Resource
  private DiscoveryClient discoveryClient;

  @RequestMapping("/value")
  public String value() {
    return "ip: " + ip + "\n" + "mysqlPassword: " + mysqlPassword;
  }

  @RequestMapping("/env")
  public String env() {
    return "ip: " + environment.getProperty("ip") + "\n" + "mysqlPassword: " + environment.getProperty("mysql-password");
  }

  @RequestMapping("/discovery")
  public List<ServiceInstance> serviceUrl() {
    return discoveryClient.getInstances("spring-cloud-zookeeper");
  }
}
