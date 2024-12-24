package com.cmg.spcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/23 09:13
 * @Description
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableHystrixDashboard
public class DashboardMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(DashboardMainApplication.class,args);
    }
}