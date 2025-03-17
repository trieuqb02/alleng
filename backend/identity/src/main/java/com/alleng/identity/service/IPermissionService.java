package com.alleng.identity.service;

import com.alleng.identity.payload.response.PermissionMV;

import java.util.List;

public interface IPermissionService {
    List<PermissionMV> getAll();
}
