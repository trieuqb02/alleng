package com.alleng.gateway.feign;

import com.alleng.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "identity",
        configuration = FeignConfig.class
)
public interface IdentityClient {

    @GetMapping("/api/v1/auth/introspect")
    Boolean introspect(@RequestParam String token);
}
