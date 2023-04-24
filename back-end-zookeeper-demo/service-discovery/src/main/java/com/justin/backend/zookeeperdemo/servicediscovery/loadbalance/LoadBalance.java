package com.justin.backend.zookeeperdemo.servicediscovery.loadbalance;

import java.util.List;

public interface LoadBalance {
  String select(List<String> urls);
}
