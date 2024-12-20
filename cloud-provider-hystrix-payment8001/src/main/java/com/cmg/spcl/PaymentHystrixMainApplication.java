package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/19 15:37
 * @Description
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableCircuitBreaker
public class PaymentHystrixMainApplication {
    public static void main(String args[]){
        SpringApplication.run(PaymentHystrixMainApplication.class,args);
    }
}