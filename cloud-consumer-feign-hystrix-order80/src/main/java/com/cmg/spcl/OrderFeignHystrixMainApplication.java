package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/20 08:57
 * @Description
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableHystrix
public class OrderFeignHystrixMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignHystrixMainApplication.class,args);
    }
}