package com.justin.backend.zookeeperdemo.controller;

import com.justin.backend.zookeeperdemo.annotation.JustinRefreshScope;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@JustinRefreshScope
public class DemoController {
  @Value("${ip}")
  private String ip;
  @Value("${ip1}")
  private String ip1;
  @Value("${ip2}")
  private String ip2;
  @Value("${ip3}")
  private String ip3;
  @Value("${mysql-password}")
  private String mysqlPassword;

  @Resource
  private Environment environment;

  @RequestMapping("/value")
  private String value() {
    return "ip:" + ip + "\n" +
        "ip1:" + ip1 + "\n" +
        "ip2:" + ip2 + "\n" +
        "ip3:" + ip3 + "\n" +
        "mysql-password:" + mysqlPassword + "\n";
  }

  @RequestMapping("/env")
  private String env() {
    return "ip:" + environment.getProperty("ip") + "\n" +
        "ip1:" + environment.getProperty("ip1") + "\n" +
        "ip2:" + environment.getProperty("ip2") + "\n" +
        "ip3:" + environment.getProperty("ip3") + "\n" +
        "mysql-password:" + environment.getProperty("mysql-password") + "\n";
  }
}
