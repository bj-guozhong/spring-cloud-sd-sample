package com.cmg.cco.controller;

import com.cmg.cco.entities.CommonResult;
import com.cmg.cco.entities.Payment;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/1 10:33
 * @Description
 */
@RestController
public class OrderController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    public static final String PAYMENT_URL = "http://localhost:8001";
    // 改为eureka中的服务名,需要启动负载均衡
    //public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/scs/payment/create",payment,CommonResult.class);
    }

    @RequestMapping(value = "/consumer/payment/searchById",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult searchById(HttpServletRequest request, @RequestParam("sid") String sid){
        LOGGER.info("this is consumer service to get payment info.");
        return restTemplate.getForObject(PAYMENT_URL+"/scs/payment/searchById?sid="+sid,CommonResult.class);
    }

}
