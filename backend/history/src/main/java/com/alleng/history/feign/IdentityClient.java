package com.alleng.history.feign;

import com.alleng.history.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "identity",
        configuration = FeignConfig.class,
        fallback = IdentityFallBack.class
)
public interface IdentityClient {

    @GetMapping("/api/v1/auth/public-key/{username}")
    String getPublicKey(@PathVariable(name = "username") String username);

}
