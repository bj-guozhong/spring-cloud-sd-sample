package com.cmg.cco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/6 15:00
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderZk80MainApplication {
    public static void main(String [] args){
        SpringApplication.run(OrderZk80MainApplication.class,args);
    }
}
