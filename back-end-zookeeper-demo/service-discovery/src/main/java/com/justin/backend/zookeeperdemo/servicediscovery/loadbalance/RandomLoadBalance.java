package com.justin.backend.zookeeperdemo.servicediscovery.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance{
  @Override
  public String select(List<String> urls) {
    int len = urls.size();
    Random random = new Random();
    return urls.get(random.nextInt(len));
  }
}
