package com.justin.backend.zookeeperdemo.processor;

import com.justin.backend.zookeeperdemo.annotation.JustinRefreshScope;
import com.justin.backend.zookeeperdemo.model.FieldDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JustinRefreshScopeBeanPostProcessor implements BeanPostProcessor {
  private Map<String, FieldDetail> fieldDetailMap = new HashMap<>();

  public Map<String, FieldDetail> getFieldDetailMap() {
    return fieldDetailMap;
  }

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
    }
    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
  }
}
