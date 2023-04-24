package com.justin.backend.zookeeperdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDetail {
  private Field field;

  private Object instance;
}
