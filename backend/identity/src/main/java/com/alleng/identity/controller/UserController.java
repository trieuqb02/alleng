package com.alleng.identity.controller;

import com.alleng.commonlibrary.constant.ApiConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.identity.payload.request.UserVm;
import com.alleng.identity.payload.response.UserMV;
import com.alleng.identity.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1/users}/users")
public class UserController {

    IUserService userService;

    @PreAuthorize("READ_USER")
    @GetMapping("")
    public ResponseEntity<ApiVM<List<UserMV>>> getUsers(){
        List<UserMV> list = userService.getAll();
        ApiVM<List<UserMV>> apiVM = new ApiVM<>(ApiConstant.CODE_200, "Get user list successful", list);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping("/info")
    public ResponseEntity<ApiVM<UserMV>> getUser(@RequestParam UUID userId){
        UserMV userMV = userService.getUser(userId);
        ApiVM<UserMV> apiVM = new ApiVM<>(ApiConstant.CODE_200, "Get user successful", userMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("UPDATE_USER")
    @PutMapping("/update")
    public ResponseEntity<ApiVM<UserMV>> updateUser(@RequestParam UUID userId, @RequestBody UserVm userVm){
        UserMV userMV = userService.updateUser(userId, userVm);
        ApiVM<UserMV> apiVM = new ApiVM<>(ApiConstant.CODE_200, "update user successful", userMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
