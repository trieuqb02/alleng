package com.alleng.identity.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.BadCredentialsException;
import com.alleng.commonlibrary.exception.BadRequestException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.commonlibrary.exception.TokenException;
import com.alleng.commonlibrary.util.JwtUtilCommon;
import com.alleng.identity.constant.Provider;
import com.alleng.identity.constant.TokenType;
import com.alleng.identity.entity.KeyStore;
import com.alleng.identity.entity.Role;
import com.alleng.identity.entity.User;
import com.alleng.identity.payload.request.LoginVM;
import com.alleng.identity.payload.request.UserVm;
import com.alleng.identity.payload.response.SetTokenMV;
import com.alleng.identity.repository.KeyStoreRepository;
import com.alleng.identity.repository.RoleRepository;
import com.alleng.identity.repository.UserRepository;
import com.alleng.identity.utils.JwtUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements IAuthService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;

    KeyStoreRepository keyStoreRepository;

    JwtUtil jwtUtil;

    JwtUtilCommon jwtUtilCommon;

    @Transactional
    @Override
    public SetTokenMV login(LoginVM loginVM) {

        User user = userRepository.findByUsername(loginVM.username())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USERNAME_NOT_FOUND, loginVM.username()));

        boolean checkPassword = passwordEncoder.matches(loginVM.password(), user.getPassword());
        if (!checkPassword) {
            throw new BadCredentialsException(ErrorCode.PASSWORD_WRONG, loginVM.password());
        }

        if (user.getKeyStore() != null) {
            keyStoreRepository.delete(user.getKeyStore());
        }

        KeyPair keyPair = jwtUtil.generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        String accessToken = jwtUtil.generateToken(privateKey, user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.generateToken(privateKey, user, TokenType.REFRESH_TOKEN);

        KeyStore keyStore = KeyStore.builder()
                .refreshToken(refreshToken)
                .privateKey(privateKey)
                .publicKey(publicKey)
                .user(user)
                .build();
        keyStoreRepository.save(keyStore);

        String[] roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);

        return new SetTokenMV(TokenType.BEARER, accessToken, refreshToken, roles);

    }

    @Transactional
    @Override
    public SetTokenMV register(UserVm userVm) {
        boolean checkUsername = userRepository.existsByUsername(userVm.username());
        if (checkUsername) {
            throw new BadRequestException(ErrorCode.USERNAME_EXIST, userVm.username());
        }

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROLE_NOT_FOUND));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .username(userVm.username())
                .email(userVm.email())
                .provider(Provider.DEFAULT)
                .password(passwordEncoder.encode(userVm.newPassword()))
                .fullName(userVm.fullName())
                .roles(roles)
                .build();
        userRepository.save(user);

        return login(new LoginVM(userVm.username(), userVm.newPassword()));
    }

    @Transactional
    @Override
    public void logout(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, userId));

        keyStoreRepository.delete(user.getKeyStore());
    }

    @Transactional(readOnly = true)
    @Override
    public boolean introspect(String token) {
        try {
            JWTClaimsSet claimsSet = jwtUtilCommon.decodeToken(token);
            String userId = claimsSet.getSubject();
            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, userId));
            return jwtUtil.verifyToken(user.getKeyStore().getPublicKey(), token);

        } catch (ParseException e) {
            throw new TokenException(ErrorCode.DECODE_TOKEN_FAIL, token);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SetTokenMV refresh(UUID userId, String refreshToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        boolean verifyToken = jwtUtil.verifyToken(user.getKeyStore().getPublicKey(), refreshToken);
        boolean compareToken = refreshToken.equals(user.getKeyStore().getRefreshToken());

        if (compareToken && verifyToken) {
            String accessToken = jwtUtil.generateToken(user.getKeyStore().getPrivateKey(), user, TokenType.ACCESS_TOKEN);
            String[] roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);

            return new SetTokenMV(TokenType.BEARER, accessToken, refreshToken, roles);
        } else {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN, refreshToken);
        }
    }

    @Override
    public String getPublicKeyByUserId(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, userId));
        return user.getKeyStore().getPublicKey();
    }
}
