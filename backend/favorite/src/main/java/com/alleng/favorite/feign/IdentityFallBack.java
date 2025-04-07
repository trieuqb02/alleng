package com.alleng.favorite.feign;

import org.springframework.stereotype.Component;

@Component
public class IdentityFallBack implements IdentityClient {
    @Override
    public String getPublicKey(String username) {
        return "public-key unavailable!";
    }
}
