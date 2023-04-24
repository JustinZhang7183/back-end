package com.justin.backend.zookeeperdemo.servicediscovery.discovery;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery{
  private final CuratorFramework curatorFramework;

  private final String basePath;

  public ZkServiceDiscovery(String basePath, String zkServer) {
    this.curatorFramework = CuratorFrameworkFactory
        .builder()
        .connectionTimeoutMs(20000)
        .connectString(zkServer)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();
    this.basePath = basePath;
    curatorFramework.start();
  }

  @Override
  public List<String> discovery(String serviceName) {
    String serviveNamePath = basePath + "/" + serviceName;
    try {
      if (curatorFramework.checkExists().forPath(serviveNamePath) != null) {
        return this.curatorFramework.getChildren().forPath(serviveNamePath);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void registerWatch(String serviceNamePath) {
    CuratorCache curatorCache = CuratorCache.build(curatorFramework, serviceNamePath);
    CuratorCacheListener listener = CuratorCacheListener.builder().forPathChildrenCache(serviceNamePath, curatorFramework, new PathChildrenCacheListener() {
      @Override
      public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
        log.info("the latest urls: ");
        for (String s : curatorFramework.getChildren().forPath(serviceNamePath)) {
          log.info(s);
        }
      }
    }).build();
    curatorCache.listenable().addListener(listener);
    curatorCache.start();
  }
}
