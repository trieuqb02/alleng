package com.alleng.news.config;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.CustomException;
import com.alleng.commonlibrary.exception.Oauth2AuthenticationEntryPoint;
import com.alleng.commonlibrary.util.JwtUtilCommon;
import com.alleng.news.feign.IdentityClient;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${com.alleng.prefix.api}")
    private String prefixApi;

    @Value("#{'${com.alleng.public.endpoints}'.split(',')}")
    private List<String> publicEndpoints;

    @Qualifier("delegatedAuthenticationEntryPoint")
    private final Oauth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;

    private final JwtUtilCommon jwtUtilCommon;

    private final IdentityClient identityClient;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> formattedEndpoints = publicEndpoints.stream()
                .map(endpoint -> prefixApi + endpoint)
                .toList();
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(formattedEndpoints.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(oauth2AuthenticationEntryPoint)
                )
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                JWTClaimsSet claimsSet = jwtUtilCommon.decodeToken(token);
                System.out.println(claimsSet.getSubject());
                String publicKeyBase64 = identityClient.getPublicKey(claimsSet.getSubject());

                PublicKey rsaPublicKey = jwtUtilCommon.getPublicKeyFromBase64(publicKeyBase64);

                return NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaPublicKey).build().decode(token);
            } catch (ParseException parseException) {
                CustomException customException = new CustomException(ErrorCode.DECODE_TOKEN_FAIL, token);
                throw new OAuth2AuthenticationException(new OAuth2Error(customException.getErrorCode().getCode(), customException.getMessage(), null));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException specException) {
                CustomException customException = new CustomException(ErrorCode.INVALID_TOKEN, token);
                throw new OAuth2AuthenticationException(new OAuth2Error(customException.getErrorCode().getCode(), customException.getMessage(), null));
            }
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authenticationConverter;
    }
}