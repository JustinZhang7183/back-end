package com.justin.backend.practice.controller;

import com.justin.backend.common.utils.NumberUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: test controller.
 *
 * @author Justin_Zhang
 * @date 2/8/2023 16:16
 */
@RestController
@RequestMapping("/test")
public class TestController {

  @RequestMapping("/")
  public Integer test() {
    return NumberUtil.test() + 2;
  }
}
