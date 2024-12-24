package com.cmg.spcl.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/24 10:41
 * @Description
 */
@Configuration
public class GateWayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        //代码配置路由
        routes.route("path_route_payment_get",
                r -> r.path("/news")
                        .uri("https://www.google.com/news")).build();

        return routes.build();
    }
}
