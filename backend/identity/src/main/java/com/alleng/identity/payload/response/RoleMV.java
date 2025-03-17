package com.alleng.identity.payload.response;

import com.alleng.identity.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleMV(UUID id, String name, String description, Set<PermissionMV> permissions) {
    public static RoleMV convertRoleMV(Role role) {
        Set<PermissionMV> permissionMVS = role.getPermissions().stream()
                .map(PermissionMV::convertPermissionMV)
                .collect(Collectors.toSet());
        return new RoleMV(role.getId(), role.getName(), role.getDescription(), permissionMVS);
    }
}
