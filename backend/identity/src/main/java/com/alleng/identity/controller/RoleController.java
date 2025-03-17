package com.alleng.identity.controller;

import com.alleng.commonlibrary.constant.ApiConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.identity.payload.request.RoleVM;
import com.alleng.identity.payload.response.RoleMV;
import com.alleng.identity.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1/roles}/roles")
public class RoleController {

    IRoleService roleService;

    @PreAuthorize("READ_ROLES")
    @GetMapping("")
    public ResponseEntity<ApiVM<List<RoleMV>>> getAll(){
        List<RoleMV> mvList = roleService.getAll();
        ApiVM<List<RoleMV>> apiVM = new ApiVM<>(ApiConstant.CODE_200, ApiConstant.OK, mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("WRITE_ROLE")
    @PostMapping("")
    public ResponseEntity<ApiVM<RoleMV>> createRole(@RequestBody RoleVM roleVM){
        RoleMV roleMV = roleService.createRole(roleVM);
        ApiVM<RoleMV> apiVM = new ApiVM<>(ApiConstant.CODE_201, ApiConstant.CREATED, roleMV);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @PreAuthorize("READ_ROLE")
    @GetMapping("{roleId}")
    public ResponseEntity<ApiVM<RoleMV>> getRole(@PathVariable("roleId") UUID id){
        RoleMV roleMV = roleService.getRole(id);
        ApiVM<RoleMV> apiVM = new ApiVM<>(ApiConstant.CODE_200, ApiConstant.OK, roleMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("UPDATE_ROLE")
    @PutMapping("/{roleId}")
    public ResponseEntity<ApiVM<RoleMV>> updateRole(@PathVariable("roleId") UUID id, @RequestBody RoleVM roleVM){
        RoleMV roleMV = roleService.updateRole(id, roleVM);
        ApiVM<RoleMV> apiVM = new ApiVM<>(ApiConstant.CODE_200, ApiConstant.OK, roleMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("DELETE_ROLE")
    @DeleteMapping("")
    public ResponseEntity<ApiVM<String>> deleteRoles(@RequestBody List<UUID> idList){
        roleService.delete(idList);
        ApiVM<String> apiVM = new ApiVM<>(ApiConstant.CODE_200, "Delete successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
