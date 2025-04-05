package com.alleng.gateway.filter;

import com.alleng.gateway.feign.IdentityClient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthFilter implements GatewayFilter, Ordered {

    IdentityClient identityClient;

    public AuthFilter(@Lazy IdentityClient identityClient) {
        this.identityClient = identityClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        if (authMissing(request)) {
            return onError(exchange);
        }

        String token = request.getHeaders().getOrEmpty("Authorization").getFirst();

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            Boolean result = identityClient.introspect(token);

            if (result) {
                return chain.filter(exchange);
            }
        }
        return onError(exchange);

    }

    @Override
    public int getOrder() {
        return RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 1;
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String errorJson = String.format("{\"error\": \"%s\"}", "Authentication Failure!");
        byte[] bytes = errorJson.getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
