package com.alleng.history.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 3);
    }

    @Bean
    public Request.Options feignRequestOptions() {
        return new Request.Options(Duration.ofSeconds(5), Duration.ofSeconds(10), false);
    }

}
