package com.alleng.identity.payload.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record RoleVM(@NotBlank String name, String description, List<UUID> permissions) { }
