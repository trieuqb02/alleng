package com.alleng.payment.feign;

import com.alleng.payment.config.IdentityConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "identity",
        configuration = IdentityConfig.class,
        fallback = IdentityFallBack.class
)
public interface IdentityClient {

    @GetMapping("/api/v1/auth/public-key/{userId}")
    String getPublicKey(@PathVariable(name = "userId") String userId);

}
