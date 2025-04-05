package com.alleng.identity.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.BadRequestException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.identity.entity.KeyStore;
import com.alleng.identity.entity.User;
import com.alleng.identity.payload.request.UserVm;
import com.alleng.identity.payload.response.UserMV;
import com.alleng.identity.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements IUserService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserMV> getAll() {
        return userRepository.findAll().stream().map(UserMV::convertUserMV).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserMV getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, userId));
        return UserMV.convertUserMV(user);
    }

    @Transactional
    @Override
    public UserMV updateUser(UUID userId, UserVm userVm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, userId));

        user.setFullName(userVm.fullName());
        user.setEmail(user.getEmail());
        if(userVm.newPassword() != null){
            boolean checkPassword = passwordEncoder.matches(userVm.oldPassword(), user.getPassword());
            if(checkPassword){
                user.setPassword(passwordEncoder.encode(userVm.newPassword()));
            } else {
                throw new BadRequestException(ErrorCode.PASSWORD_WRONG);
            }
        }

        return UserMV.convertUserMV(userRepository.save(user));
    }

    @Override
    public KeyStore getKeyStoreFromUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USERNAME_NOT_FOUND, username));
        return user.getKeyStore();
    }
}
