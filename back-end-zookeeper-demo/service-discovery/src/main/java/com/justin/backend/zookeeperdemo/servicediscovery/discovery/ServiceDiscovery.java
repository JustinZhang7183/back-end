package com.justin.backend.zookeeperdemo.servicediscovery.discovery;

import java.util.List;

public interface ServiceDiscovery {
  List<String> discovery(String serviceName);

  void registerWatch(String serviceNamePath);
}
