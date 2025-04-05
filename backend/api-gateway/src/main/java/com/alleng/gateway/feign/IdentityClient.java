package com.alleng.gateway.feign;

import com.alleng.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "identity",
        url = "http://localhost:8080/api/v1/auth",
        configuration = FeignConfig.class
)
public interface IdentityClient {

    @GetMapping("/introspect")
    Boolean introspect(@RequestParam String token);
}
