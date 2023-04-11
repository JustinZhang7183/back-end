package com.justin.backend.springsecuritydemo.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Order determine the bean order in list which inject into other class.
 */
@Data
@Order(3)
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class OrderTest {
    private String beanName;

    @Order(10)
    @Bean(name = "orderTestOne")
    public OrderTest orderTestOne() {
        return new OrderTest("orderTestOne");
    }

    @Order(2)
    @Bean(name = "orderTestTwo")
    public OrderTest orderTestTwo() {
        return new OrderTest("orderTestTwo");
    }
}
