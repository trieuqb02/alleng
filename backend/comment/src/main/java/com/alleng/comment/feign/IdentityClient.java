package com.alleng.comment.feign;

import com.alleng.comment.config.FeignConfig;
import com.alleng.comment.payload.response.UserMV;
import com.alleng.commonlibrary.payload.ApiVM;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "identity",
        configuration = FeignConfig.class,
        fallback = IdentityFallBack.class
)
public interface IdentityClient {

    @GetMapping("/api/v1/auth/public-key/{username}")
    String getPublicKey(@PathVariable(name = "username") String username);

    @GetMapping("/api/v1/users/list/ids")
    ResponseEntity<ApiVM<List<UserMV>>> getListUser(@RequestParam("ids") List<UUID> list);

}
