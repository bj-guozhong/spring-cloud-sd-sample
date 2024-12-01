package com.cmg.cco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/1 10:13
 * @Description
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class,args);
    }
}