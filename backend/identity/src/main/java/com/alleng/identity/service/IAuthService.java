package com.alleng.identity.service;

import com.alleng.identity.payload.request.LoginVM;
import com.alleng.identity.payload.request.UserVm;
import com.alleng.identity.payload.response.SetTokenMV;

import java.util.UUID;

public interface IAuthService {
    SetTokenMV login(LoginVM loginVM);

    SetTokenMV register(UserVm userVm);

    void logout(UUID userId);

    boolean introspect(String token);

    SetTokenMV refresh(UUID userId, String token);
}
