package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/26 14:15
 * @Description
 */
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientMainApplication3366 {
    public static void main(String args[]){
        SpringApplication.run(ConfigClientMainApplication3366.class,args);
    }
}