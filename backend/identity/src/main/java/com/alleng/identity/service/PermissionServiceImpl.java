package com.alleng.identity.service;

import com.alleng.identity.payload.request.RoleVM;
import com.alleng.identity.payload.response.PermissionMV;
import com.alleng.identity.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PermissionServiceImpl implements IPermissionService {

    PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<PermissionMV> getAll() {
        return permissionRepository.findAll().stream()
                .map(PermissionMV::convertPermissionMV)
                .toList();
    }
}
