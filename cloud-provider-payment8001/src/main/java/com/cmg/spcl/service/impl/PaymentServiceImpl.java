package com.cmg.spcl.service.impl;

import com.cmg.spcl.dao.PaymentDao;
import com.cmg.spcl.entities.Payment;
import com.cmg.spcl.service.PaymentService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;

    public int create(Payment payment){
        return paymentDao.create(payment);
    }

    public Payment getPaymentById(@Param("sid") String sid){
        return paymentDao.getPaymentById(sid);
    }
}
