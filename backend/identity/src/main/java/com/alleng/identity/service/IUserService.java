package com.alleng.identity.service;

import com.alleng.identity.entity.KeyStore;
import com.alleng.identity.payload.request.UserVm;
import com.alleng.identity.payload.response.UserMV;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    List<UserMV> getAll();

    UserMV getUser(UUID userId);

    UserMV updateUser(UUID userId, UserVm userVm);

    KeyStore getKeyStoreFromUsername(String username);
}
