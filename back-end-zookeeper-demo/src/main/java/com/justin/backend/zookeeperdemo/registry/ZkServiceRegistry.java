package com.justin.backend.zookeeperdemo.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;

@Slf4j
public class ZkServiceRegistry implements ServiceRegistry{
  private CuratorFramework curatorFramework;

  private final String serviceName;

  private final String basePath;

  private final String ip;

  private final String port;

  public ZkServiceRegistry(String serviceName, String basePath, String zkServer, String ip, String port) throws IOException {
    this.ip = ip;
    this.port = port;
    this.serviceName = serviceName;
    this.basePath = basePath;
    this.curatorFramework = CuratorFrameworkFactory
        .builder()
        .connectionTimeoutMs(20000)
        .connectString(zkServer)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();
    curatorFramework.start();
  }

  @Override
  public void register() {
    String serviceNamePath = basePath + "/" + serviceName;
    try {
      if (curatorFramework.checkExists().forPath(serviceNamePath) == null) {
        // create permanent node as service name
        this.curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serviceNamePath);
      }
      String urlPath = serviceNamePath + "/" + ip + ":" + port;
      if (curatorFramework.checkExists().forPath(urlPath) == null) {
        String urlNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(urlPath);
        log.info("service node: {} register successfully!", urlNode);
      }
    }  catch (Exception e) {
      e.printStackTrace();
    }
  }
}
