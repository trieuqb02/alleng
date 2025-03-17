package com.alleng.identity.payload.response;

import com.alleng.identity.entity.Permission;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionMV(UUID id, String name, String description) {

    public static PermissionMV convertPermissionMV(Permission permission) {
        return new PermissionMV(permission.getId(), permission.getName(), permission.getDescription());
    }
}
