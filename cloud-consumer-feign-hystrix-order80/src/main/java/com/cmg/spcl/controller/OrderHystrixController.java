package com.cmg.spcl.controller;

import com.cmg.spcl.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/20 09:06
 * @Description
 */
@RestController
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderHystrixController.class);

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_Ok(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_Ok(id);
        LOGGER.info("this is order hystrix controller result:"+result);
        return result;
    }

    //超时，配置超时不再等待，另寻解决方案。
/*    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000")
    })*/
    @HystrixCommand
    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_timeout(@PathVariable("id") Integer id){

        int age = 10/0;
        String result = paymentHystrixService.paymentInfo_timeout(id);
        LOGGER.info("this is order hystrix controller timeout result:"+result);
        return result;
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "Thread: "+Thread.currentThread().getName()+" order hystrix  paymentInfo_TimeoutHandler,id is :"+id+",80系统繁忙，请稍后再试。。。。";
    }

    //下面是全局fallback
    public String payment_Global_FallbackMethod(){
        return "Global 异常处理信息，请稍后再试。。。";
    }
}
