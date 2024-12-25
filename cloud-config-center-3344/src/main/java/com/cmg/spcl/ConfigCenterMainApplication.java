package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @Author zgz
 * @Date 2024/12/25 09:31
 * @version 1.0
 * @Description 
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigCenterMainApplication {
    public static void main(String args[]){
        SpringApplication.run(ConfigCenterMainApplication.class,args);
    }
}