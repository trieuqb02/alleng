package com.alleng.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

// get a Key to use in the Rate Limit
public class ClientAddressResolverConfig implements KeyResolver {

    // return ip address of the client
    // ServerWebExchange contains info about HTTP request
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .map(Mono::just)
                .orElse(Mono.empty());
    }
}
