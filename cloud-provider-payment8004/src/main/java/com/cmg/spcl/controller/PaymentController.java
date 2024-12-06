package com.cmg.spcl.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/6 10:57
 * @Description
 */
@RestController
public class PaymentController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value="/payment/zk")
    public String paymentzk(){
        LOGGER.info("spring cloud with zookeeper come in paymentzk method!");
        return "spring cloud with zookeeper:"+serverPort+"\t"+ UUID.randomUUID().toString();
    }
}
