package com.alleng.identity.utils;

import com.alleng.commonlibrary.constant.ApiConstant;
import com.alleng.commonlibrary.exception.BadRequestException;
import com.alleng.identity.constant.TokenType;
import com.alleng.identity.entity.KeyStore;
import com.alleng.identity.entity.Permission;
import com.alleng.identity.entity.User;
import com.alleng.identity.repository.UserRepository;
import com.alleng.identity.service.IUserService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000;// 1 house
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 day

    IUserService userService;

    public String generateToken(String privateKeyBase64, User user, TokenType tokenType) {
        try {
            // Decode privateKey form base64
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // Header Token
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.RS256);

            // Payload Token
            JWTClaimsSet jwtClaimsSet = claimsSet(user, tokenType);

            // Signature Token
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            signedJWT.sign(new RSASSASigner(privateKey));

            // Convert to String
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new BadRequestException(ApiConstant.CODE_400, ApiConstant.ERROR_TOKEN);
        }
    }

    private JWTClaimsSet claimsSet(User user, TokenType tokenType) {
        if (tokenType.equals(TokenType.ACCESS_TOKEN)) {
            return new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("alleng.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                    .claim("fullName", user.getFullName())
                    .claim("thumbnail", user.getThumbnail())
                    .claim("scope", user.getRoles().stream()
                            .flatMap(role -> role.getPermissions().stream().map(Permission::getName))
                            .collect(Collectors.joining(" ")))
                    .build();
        } else if (tokenType.equals(TokenType.REFRESH_TOKEN)) {
            return new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("alleng.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                    .build();
        }
        return new JWTClaimsSet.Builder().build();
    }

    public boolean verifyToken(String publicKeyBase64, String token) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(new RSASSAVerifier((RSAPublicKey) publicKey));
        } catch (Exception e){
            throw new BadRequestException(ApiConstant.CODE_400, ApiConstant.ERROR_VERIFY_TOKEN);
        }
    }

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public JWTClaimsSet decodeToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet();
    }

    public KeyStore getKeyStore(String username){
        return userService.getKeyStoreFromUsername(username);
    }

    public PublicKey getPublicKeyFromBase64(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
       return keyFactory.generatePublic(keySpec);
    };

    public PrivateKey getPrivateKeyFromBase64(String privateKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    };
}
