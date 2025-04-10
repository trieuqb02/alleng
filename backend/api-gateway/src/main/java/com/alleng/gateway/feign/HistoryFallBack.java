package com.alleng.gateway.feign;

import com.alleng.gateway.payload.response.ApiVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HistoryFallBack implements HistoryClient {
    @Override
    public ResponseEntity<ApiVM<Long>> countTheTime(String authHeader) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiVM<>(3L));
    }
}
