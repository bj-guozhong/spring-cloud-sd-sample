package com.cmg.spcl.service;

import com.cmg.spcl.entities.Payment;
import org.apache.ibatis.annotations.Param;

public interface PaymentService {

    public int create(Payment payment);

    public Payment getPaymentById(@Param("sid") String sid);
}
