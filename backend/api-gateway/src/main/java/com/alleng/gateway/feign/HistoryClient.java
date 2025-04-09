package com.alleng.gateway.feign;

import com.alleng.gateway.config.FeignConfig;
import com.alleng.gateway.payload.response.ApiVM;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "history",
        configuration = FeignConfig.class
)
public interface HistoryClient {
    @GetMapping("/api/v1/history/news/count")
    ResponseEntity<ApiVM<Long>> countTheTime(@RequestHeader(value = "Authorization") String authHeader);
}
