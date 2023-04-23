package com.justin.backend.zookeeperdemo.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justin.backend.zookeeperdemo.model.FieldDetail;
import com.justin.backend.zookeeperdemo.processor.JustinRefreshScopeBeanPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class ZkConfigApplicationContextInitializer implements ApplicationContextInitializer {
  private static final String REMOTE_ENV = "remote-env";

  @Override
  public void initialize(ConfigurableApplicationContext context) {
    // connect zookeeper
    ConfigurableEnvironment environment = context.getEnvironment();
    String url = environment.getProperty("zookeeper-url");
    String path = environment.getProperty("config-key");
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

      // permanent listening
      CuratorCache curatorCache = CuratorCache.build(curatorFramework, path, CuratorCache.Options.SINGLE_NODE_CACHE);
      CuratorCacheListener listener = CuratorCacheListener.builder().forAll((type, oldData, newData) -> {
        if (type.equals(CuratorCacheListener.Type.NODE_CHANGED)) {
          log.info("properties was updated");
          try {
            // update environment
            Map<String, Object> updateMap = new ObjectMapper().readValue(new String(newData.getData()), Map.class);
            environment.getPropertySources().replace(REMOTE_ENV, new MapPropertySource(REMOTE_ENV, updateMap));
            // update value modified by value annotation
            JustinRefreshScopeBeanPostProcessor processor = context.getBean("justinRefreshScopeBeanPostProcessor", JustinRefreshScopeBeanPostProcessor.class);
            Map<String, FieldDetail> fieldDetailMap = processor.getFieldDetailMap();
            for (String key : fieldDetailMap.keySet()) {
              if(updateMap.containsKey(key)){
                String value = environment.getProperty(key);
                Field field = fieldDetailMap.get(key).getField();
                field.setAccessible(true);
                field.set(fieldDetailMap.get(key).getInstance(),value);
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).build();
      curatorCache.listenable().addListener(listener);
      curatorCache.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
