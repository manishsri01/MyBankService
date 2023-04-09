package org.my.bank.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class PreFilterConfig implements GlobalFilter {

    private static final List<String> allowEndpoints = List.of(
            "/customer/signup",
            "/customer/login"
    );

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange);

            final String token = this.getAuthHeader(request);

            if (jwtConfig.isInvalid(token))
                return this.onError(exchange);

            this.forwardCustomerHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }


    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private String  getAuthHeader(ServerHttpRequest request) {
        String headerAuth = request.getHeaders().getOrEmpty("Authorization").get(0);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void forwardCustomerHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtConfig.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("X-FORWARDED-CUSTOMER-ID", claims.getId())
                .header("X-FORWARDED-CUSTOMER-NAME", claims.getSubject())
                .build();
    }

    private final Predicate<ServerHttpRequest> isSecured =
            request -> allowEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}

