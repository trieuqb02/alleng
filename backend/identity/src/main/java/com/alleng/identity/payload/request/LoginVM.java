package com.alleng.identity.payload.request;

import jakarta.validation.constraints.NotBlank;

public record LoginVM(@NotBlank String username,@NotBlank String password) {
}
