package com.cmg.spcl.service;

import com.cmg.cco.entities.CommonResult;
import com.cmg.cco.entities.Payment;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/19 11:01
 * @Description
 */
@Component
@FeignClient(value="CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {
    @GetMapping(value = "/scs/payment/searchById", produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResult<Payment> searchById(@RequestParam("sid") String sid);

    @GetMapping(value="/scs/payment/feign/timeout")
    public String paymentFeignTimeout();
}
