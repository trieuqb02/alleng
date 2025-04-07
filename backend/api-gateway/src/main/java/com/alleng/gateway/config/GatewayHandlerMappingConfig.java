package com.alleng.gateway.config;

import com.alleng.gateway.filter.AuthFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GatewayHandlerMappingConfig {

    AuthFilter authFilter;

    @Bean
    public KeyResolver keyResolver() {
        return new ClientAddressResolverConfig();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 10, 4);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("identity-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver()))
                                .circuitBreaker(c -> c
                                        .setName("identity")))
                        .uri("lb://identity"))
                .route("identity-service", r -> r.path("/api/v1/permissions")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(authFilter)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver()))
                        )
                        .uri("lb://identity"))
                .route("identity-service", r -> r.path(
                                "/api/v1/roles",
                                "/api/v1/roles/**")
                        .and().method(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT)
                        .filters(f -> f.filter(authFilter)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("lb://identity"))
                .route("identity-service", r -> r.path(
                                "/api/v1/users",
                                "/api/v1/users/**")
                        .and().method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT)
                        .filters(f -> f.filter(authFilter)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("lb://identity"))
                .route("news-service", r -> r.path(
                                "/api/v1/source/all",
                                "/api/v1/topic/all",
                                "/api/v1/news/{newsId}",
                                "/api/v1/news/list/rand",
                                "/api/v1/news/list"
                        )
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.requestRateLimiter(c -> c
                                .setRateLimiter(redisRateLimiter())
                                .setKeyResolver(keyResolver())))
                        .uri("lb://news"))
                .route("news-service", r -> r.path(
                                "/api/v1/source",
                                "/api/v1/topic",
                                "/api/v1/news",
                                "/api/v1/news/{newsId}"
                        )
                        .and().method(HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT)
                        .filters(f -> f.filter(authFilter)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("lb://news"))
                .route("file-service", r -> r.path("/api/v1/file/**")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.requestRateLimiter(c -> c
                                .setRateLimiter(redisRateLimiter())
                                .setKeyResolver(keyResolver())))
                        .uri("lb://file"))
                .route("favorite-service", r -> r.path("/api/v1/favorite/**")
                        .and().method(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.POST)
                        .filters(f -> f.filter(authFilter)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("lb://favorite"))
                .build();
    }
}
