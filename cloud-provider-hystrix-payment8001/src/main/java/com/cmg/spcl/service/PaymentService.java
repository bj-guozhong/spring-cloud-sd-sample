package com.cmg.spcl.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/19 15:43
 * @Description
 */
@Service
public class PaymentService {

    //正常访问测试
    public String paymentInfo_Ok(Integer id){
        return "Thread: "+Thread.currentThread().getName()+" paymentInfo_Ok,id is :"+id;
    }

    //超时，配置超时不再等待，另寻解决方案。
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="3000")
    })
    public String paymentInfo_Timeout(Integer id){

        //3秒以内正常，5秒出错
        int timeNumber = 5;
        //主动计算错误，测试
        //int age = 10/0;
        try{
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "Thread: "+Thread.currentThread().getName()+" paymentInfo_Timeout,id is :"+id+" 耗时"+timeNumber+"秒钟。";
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "Thread: "+Thread.currentThread().getName()+" paymentInfo_TimeoutHandler,id is :"+id+",8001系统繁忙，请稍后再试。。。。";
    }

    //服务熔断,circuitBreaker.enabled 是否开启断路器，requestVolumeThreshold 请求次数，sleepWindowInMilliseconds 时间窗口期，errorThresholdPercentage 失败率达到多少跳闸
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled",value="true"),
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="60")
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if(id<0){
            throw new RuntimeException("---- id 不能为负数");
        }
        String seriaNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t"+" 调用成功，流水号：：："+seriaNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能为负数，请稍后再试。。。。id:"+id;
    }

}
