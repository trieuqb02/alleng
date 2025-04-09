package com.alleng.comment.feign;

import com.alleng.comment.payload.response.UserMV;
import com.alleng.commonlibrary.payload.ApiVM;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdentityFallBack implements IdentityClient {
    @Override
    public String getPublicKey(String username) {
        return "public-key unavailable!";
    }

    @Override
    public ResponseEntity<ApiVM<List<UserMV>>> getListUser(List<String> list) {
        return ResponseEntity.ok(null);
    }
}
