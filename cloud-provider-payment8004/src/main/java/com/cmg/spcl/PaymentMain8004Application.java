package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/6 10:54
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8004Application {
    public static void main(String args[]){
        SpringApplication.run(PaymentMain8004Application.class,args);
    }
}
