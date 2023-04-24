package com.justin.backend.zookeeperdemo.listener;

import com.justin.backend.zookeeperdemo.registry.ServiceRegistry;
import com.justin.backend.zookeeperdemo.registry.ZkServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Slf4j
public class ZkApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("context refreshed ...");
    Environment environment = event.getApplicationContext().getEnvironment();
    String zkServer = environment.getProperty("zookeeper-url") + ":2181";
    String path = environment.getProperty("services-path");
    String serviceName = environment.getProperty("spring.application.name");
    String ip = environment.getProperty("server.ip");
    String port = environment.getProperty("server.port");
    try {
      ServiceRegistry serviceRegistry = new ZkServiceRegistry(serviceName, path, zkServer, ip, port);
      serviceRegistry.register();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
