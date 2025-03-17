package com.alleng.identity.config;

import com.alleng.commonlibrary.constant.ApiConstant;
import com.alleng.identity.entity.KeyStore;
import com.alleng.identity.security.Oauth2AuthenticationEntryPoint;
import com.alleng.identity.utils.JwtUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
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

    private final JwtUtil jwtUtil;

    private final Oauth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
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
                JWTClaimsSet claimsSet = jwtUtil.decodeToken(token);

                String username = claimsSet.getSubject();

                KeyStore keyStore = jwtUtil.getKeyStore(username);

                PublicKey rsaPublicKey = jwtUtil.getPublicKeyFromBase64(keyStore.getPublicKey());

                return NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaPublicKey).build().decode(token);
            } catch (Exception e) {
                throw new OAuth2AuthenticationException(new OAuth2Error(ApiConstant.CODE_401, e.getMessage(), null));
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