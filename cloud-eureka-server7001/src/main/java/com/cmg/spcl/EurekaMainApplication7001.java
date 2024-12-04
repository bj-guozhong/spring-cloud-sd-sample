package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/2 16:48
 * @Description
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaMainApplication7001 {
    public static void main(String args[]){
        SpringApplication.run(EurekaMainApplication7001.class,args);
    }
}
