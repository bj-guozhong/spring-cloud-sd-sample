package com.cmg.spcl.controller;

import com.cmg.cco.entities.CommonResult;
import com.cmg.cco.entities.Payment;
import com.cmg.spcl.service.PaymentFeignService;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/19 11:08
 * @Description
 */
@RestController
public class OrderFeignController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderFeignController.class);

    @Resource
    private PaymentFeignService paymentFeignService;

    @RequestMapping(value = "/payment/searchById",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult<Payment> searchById(HttpServletRequest request, @RequestParam("sid") String sid){
        LOGGER.info("====== this is by feign controller output content sid:::::"+sid);
        return paymentFeignService.searchById(sid);
    }

    @GetMapping(value="/consumer/payment/feign/timeout")
    public String paymentFeignTimeout(){
        return paymentFeignService.paymentFeignTimeout();
    }
}
