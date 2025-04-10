package com.alleng.comment.feign;

import com.alleng.comment.payload.response.UserMV;
import com.alleng.commonlibrary.payload.ApiVM;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class IdentityFallBack implements IdentityClient {
    @Override
    public String getPublicKey(String username) {
        return "public-key unavailable!";
    }

    @Override
    public ResponseEntity<ApiVM<List<UserMV>>> getListUser(List<UUID> list) {
        return ResponseEntity.ok(null);
    }
}
