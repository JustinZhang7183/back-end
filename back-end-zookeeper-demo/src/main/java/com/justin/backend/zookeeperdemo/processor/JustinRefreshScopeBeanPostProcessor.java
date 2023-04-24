package com.justin.backend.zookeeperdemo.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justin.backend.zookeeperdemo.annotation.JustinRefreshScope;
import com.justin.backend.zookeeperdemo.model.FieldDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JustinRefreshScopeBeanPostProcessor implements BeanPostProcessor {
  private static final String REMOTE_ENV = "remote-env";

  private Map<String, FieldDetail> fieldDetailMap = new HashMap<>();

  public Map<String, FieldDetail> getFieldDetailMap() {
    return fieldDetailMap;
  }

  @Autowired
  private ConfigurableApplicationContext context;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Class<?> clazz = bean.getClass();
    if (clazz.isAnnotationPresent(JustinRefreshScope.class)) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Value.class)) {
          Value value = field.getAnnotation(Value.class);
          String val = value.value();
          val = val.substring(2, val.indexOf("}"));
          log.info("val: {}", val);
          this.fieldDetailMap.put(val, new FieldDetail(field, bean));
        }
      }
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
      // permanent listening
      CuratorCache curatorCache = CuratorCache.build(curatorFramework, path, CuratorCache.Options.SINGLE_NODE_CACHE);
      CuratorCacheListener listener = CuratorCacheListener.builder().forAll(new CuratorCacheListener() {
        @Override
        public void event(Type type, ChildData oldData, ChildData newData) {
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
        }
      }).build();
      curatorCache.listenable().addListener(listener);
      curatorCache.start();
    }
    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
  }
}
