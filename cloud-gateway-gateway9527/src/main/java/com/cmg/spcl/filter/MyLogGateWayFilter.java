package com.cmg.spcl.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Date;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/24 15:20
 * @Description
 */
@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {

    //    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        log.info("---------------come in here:"+new Date());

        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if(StrUtil.isEmpty(uname)){
            log.info("------------username is null,非法用户，不予放行");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder(){
        return 0;
    }
}
