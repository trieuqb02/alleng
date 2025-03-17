package com.alleng.identity.service;

import com.alleng.identity.payload.request.RoleVM;
import com.alleng.identity.payload.response.RoleMV;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    List<RoleMV> getAll();

    RoleMV createRole(RoleVM roleVM);

    RoleMV updateRole(UUID id, RoleVM roleVM);

    RoleMV getRole(UUID id);

    void delete(List<UUID> idList);
}
