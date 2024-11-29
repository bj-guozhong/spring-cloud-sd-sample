package com.cmg.spcl.crontroller;


import com.cmg.spcl.entities.CommonResult;
import com.cmg.spcl.entities.Payment;
import com.cmg.spcl.service.PaymentService;
import org.slf4j.LoggerFactory;
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

    @PostMapping(value="/payment/create")
    public CommonResult create(Payment payment){
        int result = paymentService.create(payment);
        if(result>0){
            return new CommonResult(200,"insert success!",result);
        }else{
            return new CommonResult(500,"insert error!",null);
        }
    }

    @RequestMapping(value = "/payment/searchById",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult searchById(HttpServletRequest request, @RequestParam("sid") String sid){

        LOGGER.info("ready to search info by sid:"+sid);
        Payment payment = paymentService.getPaymentById(sid);
        if(payment!=null){
            return new CommonResult(200,"search success!",payment);
        }else{
            return new CommonResult(500,"search no data:"+sid,null);
        }
    }
}
