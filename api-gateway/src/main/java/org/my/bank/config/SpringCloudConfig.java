package org.my.bank.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        System.out.println("sss");
        return builder.routes()
                .route(r -> r.path("/customer/**")
                        .filters(f -> f.addRequestHeader("X-Forwarded-Module", "Customer"))
                        .uri("http://localhost:9051/")).
                route(r -> r.path("/savings/**")
                        .filters(f -> f.addRequestHeader("X-Forwarded-Module", "Savings"))
                        .uri("http://localhost:9052/"))
                .build();

    }
}