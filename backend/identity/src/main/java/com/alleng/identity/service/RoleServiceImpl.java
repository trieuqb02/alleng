package com.alleng.identity.service;

import com.alleng.commonlibrary.constant.ApiConstant;
import com.alleng.commonlibrary.exception.BadRequestException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.identity.entity.Permission;
import com.alleng.identity.entity.Role;
import com.alleng.identity.payload.request.RoleVM;
import com.alleng.identity.payload.response.RoleMV;
import com.alleng.identity.repository.PermissionRepository;
import com.alleng.identity.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements IRoleService {

    RoleRepository roleRepository;

    PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<RoleMV> getAll() {
        return roleRepository.findAllByIsDelete(false).stream()
                .map(RoleMV::convertRoleMV)
                .toList();
    }

    @Transactional
    @Override
    public RoleMV createRole(RoleVM roleVM) {
        try {
            Role role = new Role();
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleVM.permissions()));
            role.setName(roleVM.name());
            role.setDescription(roleVM.description());
            role.setPermissions(permissions);
            return RoleMV.convertRoleMV(roleRepository.save(role));
        } catch (Exception e) {
            throw new BadRequestException(ApiConstant.CODE_400, e.getMessage());
        }
    }

    @Transactional
    @Override
    public RoleMV updateRole(UUID id, RoleVM roleVM) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiConstant.CODE_400, ApiConstant.NOT_FOUND));

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleVM.permissions()));

        role.setName(roleVM.name());
        role.setDescription(roleVM.description());
        role.setPermissions(permissions);

        return RoleMV.convertRoleMV(roleRepository.save(role));
    }

    @Transactional(readOnly = true)
    @Override
    public RoleMV getRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiConstant.CODE_400, ApiConstant.NOT_FOUND));

        return RoleMV.convertRoleMV(roleRepository.save(role));
    }

    @Transactional
    @Override
    public void delete(List<UUID> idList) {
        List<Role> roles = roleRepository.findAllById(idList).stream()
                .peek(role -> role.setDelete(true))
                .toList();
        roleRepository.saveAll(roles);
    }
}
