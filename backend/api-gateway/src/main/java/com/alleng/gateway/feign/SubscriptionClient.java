package com.alleng.gateway.feign;

import com.alleng.gateway.config.FeignConfig;
import com.alleng.gateway.constant.CodeType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "subscription",
        configuration = FeignConfig.class
)
public interface SubscriptionClient {
    @GetMapping("/api/v1/subscription/feature")
    ResponseEntity<Boolean> hasAccessToFeature(@RequestHeader(value = "Authorization") String authHeader, @RequestParam("code") CodeType code);
}
