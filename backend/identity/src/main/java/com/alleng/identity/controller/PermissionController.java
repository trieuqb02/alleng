package com.alleng.identity.controller;

import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.identity.payload.response.PermissionMV;
import com.alleng.identity.service.IPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1/permissions}/permissions")
public class PermissionController {

    IPermissionService permissionService;

    @PreAuthorize("READ_PERMISSIONS")
    @GetMapping("")
    public ResponseEntity<ApiVM<List<PermissionMV>>> getAll() {
        List<PermissionMV> mvList = permissionService.getAll();
        ApiVM<List<PermissionMV>> apiVM = new ApiVM<>(mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
