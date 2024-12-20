package com.cmg.spcl.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/20 10:32
 * @Description
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService{

    @Override
    public String paymentInfo_Ok(@PathVariable("id") Integer id){
        return "-----PaymentFallbackService fall back-paymentInfo_ok.";
    }

    @Override
    public String paymentInfo_timeout(@PathVariable("id") Integer id){
        return "-----PaymentFallbackService fall back-paymentInfo_timeout,ooooo.";
    }
}
