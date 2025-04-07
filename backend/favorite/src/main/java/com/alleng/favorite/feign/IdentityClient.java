package com.alleng.favorite.feign;

import com.alleng.favorite.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "identity-service",
        url = "http://localhost:8080/api/v1/auth",
        configuration = FeignConfig.class,
        fallback = IdentityFallBack.class
)
public interface IdentityClient {

    @GetMapping("/public-key/{username}")
    String getPublicKey(@PathVariable(name = "username") String username);

}
