package com.justin.backend.zookeeperdemo.servicediscovery;

import com.justin.backend.zookeeperdemo.servicediscovery.discovery.ServiceDiscovery;
import com.justin.backend.zookeeperdemo.servicediscovery.loadbalance.LoadBalance;
import com.justin.backend.zookeeperdemo.servicediscovery.loadbalance.RandomLoadBalance;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest
public class ServiceDiscoveryTest {
  @Resource
  private ServiceDiscovery serviceDiscovery;

  @Resource
  private Environment environment;

  private List<String> urls;

  @Test
  void discovery() throws IOException {
    urls = serviceDiscovery.discovery("zookeeper-demo-server");
    LoadBalance loadBalance = new RandomLoadBalance();
    String url = loadBalance.select(urls);
    String response = new RestTemplate().getForObject("http://" + url + "/demo/value", String.class);
    log.info("response: {}", response);
    serviceDiscovery.registerWatch(environment.getProperty("services-path") + "/zookeeper-demo-server");
    System.in.read();
  }
}
