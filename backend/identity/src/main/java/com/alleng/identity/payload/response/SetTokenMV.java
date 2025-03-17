package com.alleng.identity.payload.response;

import com.alleng.identity.constant.TokenType;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SetTokenMV(TokenType tokenType, String accessToken,  String refreshToken, String[] scopes) {

}
