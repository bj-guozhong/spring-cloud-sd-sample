package com.cmg.spcl.controller;

import com.cmg.cco.entities.CommonResult;
import com.cmg.cco.entities.Payment;
import com.cmg.spcl.service.PaymentService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/scs")
public class PaymentController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value="/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        if(result>0){
            return new CommonResult(200,"insert success!serverPort:"+serverPort,result);
        }else{
            return new CommonResult(500,"insert error!",null);
        }
    }

    @RequestMapping(value = "/payment/searchById",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult searchById(HttpServletRequest request, @RequestParam("sid") String sid){

        LOGGER.info("ready to search info by sid:"+sid);
        Payment payment = paymentService.getPaymentById(sid);
        LOGGER.info("this is test on app runnning");
        if(payment!=null){
            return new CommonResult(200,"search success!serverPort:"+serverPort,payment);
        }else{
            return new CommonResult(500,"search no data:"+sid,null);
        }
    }
}
