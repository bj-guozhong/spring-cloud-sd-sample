package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/25 10:53
 * @Description
 */
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientMainApplication {
    public static void main(String args[]){
        SpringApplication.run(ConfigClientMainApplication.class,args);
    }
}