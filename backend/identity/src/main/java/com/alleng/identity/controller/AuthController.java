package com.alleng.identity.controller;

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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1/auth}/auth")
public class AuthController {

    IAuthService authService;

    @GetMapping("/public-key/{userId}")
    public ResponseEntity<String> getPublicKey(@PathVariable String userId) {
        String publicKey = authService.getPublicKeyByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(publicKey);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiVM<SetTokenMV>> login(@RequestBody LoginVM loginVM) {
        SetTokenMV setTokenMV = authService.login(loginVM);
        ApiVM<SetTokenMV> apiVM = new ApiVM<>("user login successfully", setTokenMV);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiVM<SetTokenMV>> register(@RequestBody UserVm userVm) {
        SetTokenMV setTokenMV = authService.register(userVm);
        ApiVM<SetTokenMV> apiVM = new ApiVM<>("user register successfully", setTokenMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ApiVM<SetTokenMV>> refreshToken(@RequestParam String token, @RequestParam UUID userId) {
        SetTokenMV setTokenMV = authService.refresh(userId, token);
        ApiVM<SetTokenMV> apiVM = new ApiVM<>("refresh token successfully", setTokenMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping(value = "/introspect")
    public ResponseEntity<Boolean> introspect(@RequestParam String token) {
        boolean introspect = authService.introspect(token);
        return ResponseEntity.status(HttpStatus.OK).body(introspect);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiVM<?>> logout(@RequestParam UUID userId) {
        authService.logout(userId);
        ApiVM<?> apiVM = new ApiVM<>("user logout successfully", null);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
