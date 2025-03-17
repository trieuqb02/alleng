package com.alleng.identity.controller;

import com.alleng.commonlibrary.constant.ApiConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.identity.payload.request.LoginVM;
import com.alleng.identity.payload.request.UserVm;
import com.alleng.identity.payload.response.SetTokenMV;
import com.alleng.identity.service.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1/auth}/auth")
public class AuthController {

    IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiVM<SetTokenMV>> login(@RequestBody LoginVM loginVM){
        SetTokenMV setTokenMV = authService.login(loginVM);
        ApiVM<SetTokenMV> apiVM = new ApiVM<>(ApiConstant.CODE_200, "user login successfully" ,setTokenMV);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiVM<SetTokenMV>> register(@RequestBody UserVm userVm){
        SetTokenMV setTokenMV = authService.register(userVm);
        ApiVM<SetTokenMV> apiVM = new ApiVM<>(ApiConstant.CODE_200, "user register successfully" ,setTokenMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ApiVM<SetTokenMV>> refreshToken(@RequestParam String token, @RequestParam UUID userId){
        SetTokenMV setTokenMV = authService.refresh(userId, token);
        ApiVM<SetTokenMV> apiVM = new ApiVM<>(ApiConstant.CODE_200, "refresh token successfully" ,setTokenMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping("/introspect")
    public ResponseEntity<ApiVM<Map<String, Boolean>>> introspect(@RequestParam String token){
        boolean introspect = authService.introspect(token);
        Map<String, Boolean> responseData = Collections.singletonMap("introspect", introspect);
        ApiVM<Map<String, Boolean>> apiVM = new ApiVM<>(ApiConstant.CODE_200, "successful", responseData);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiVM<?>> logout(@RequestParam UUID userId){
        authService.logout(userId);
        ApiVM<?> apiVM = new ApiVM<>(ApiConstant.CODE_200, "user logout successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
