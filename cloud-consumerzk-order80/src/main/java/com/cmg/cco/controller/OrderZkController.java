package com.cmg.cco.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/6 15:05
 * @Description
 */
@RestController
public class OrderZkController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderZkController.class);

    public static final String INVOKE_URL = "http://cloud-payment-service8004";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value="/consumer/payment/zk")
    public String paymentInfo(){
        String result = restTemplate.getForObject(INVOKE_URL+"/payment/zk",String.class);
        return result;
    }

}
