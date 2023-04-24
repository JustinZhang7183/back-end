package com.justin.backend.zookeeperdemo.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

@Slf4j
public class ZkConfigApplicationContextInitializer implements ApplicationContextInitializer {
  private static final String REMOTE_ENV = "remote-env";

  @Override
  public void initialize(ConfigurableApplicationContext context) {
    // connect zookeeper
    ConfigurableEnvironment environment = context.getEnvironment();
    String url = environment.getProperty("zookeeper-url");
    String path = environment.getProperty("configs-path");
    log.info("initializing...");
    CuratorFramework curatorFramework = CuratorFrameworkFactory
        .builder()
        .connectionTimeoutMs(20000)
        .connectString(url)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();
    curatorFramework.start();
    try {
      // get properties
      byte[] bytes = curatorFramework.getData().forPath(path);
      Map<String, Object> map = new ObjectMapper().readValue(new String(bytes), Map.class);
      log.info("properties: {}", map);

      // load in property source
      MapPropertySource mapPropertySource = new MapPropertySource(REMOTE_ENV, map);
      environment.getPropertySources().addFirst(mapPropertySource);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
