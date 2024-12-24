package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/24 09:26
 * @Description
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayMainApplication {
    public static void main(String args[]){
        SpringApplication.run(GatewayMainApplication.class,args);
    }
}