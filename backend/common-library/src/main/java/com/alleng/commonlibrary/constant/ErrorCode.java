package com.alleng.commonlibrary.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // identity
    USERNAME_EXIST("80800", HttpStatus.CONFLICT, "Username {} exist"),
    USERNAME_NOT_FOUND("8080", HttpStatus.NOT_FOUND, "Username {} not found"),
    USER_NOT_FOUND("80801", HttpStatus.NOT_FOUND, "User with id {} not found"),
    PASSWORD_WRONG("80802", HttpStatus.UNAUTHORIZED, "Password {} wrong"),
    DECODE_TOKEN_FAIL("80803", HttpStatus.BAD_REQUEST, "Decode token {} failed"),
    INVALID_TOKEN("80804",HttpStatus.BAD_REQUEST, "Token {} invalid" ),
    ALGORITHM_TOKEN("80805", HttpStatus.INTERNAL_SERVER_ERROR, "Algorithm wrong"),
    ROLE_NOT_FOUND("80806", HttpStatus.CONFLICT, "Role {} not found"),
    ACCOUNT_NOT_FOUND("80807", HttpStatus.CONFLICT, "Account with id {} not found"),
    ROLE_NAME_EXIST("80808", HttpStatus.CONFLICT, "Role name {} exist"),

    // news
    SOURCE_NAME_EXIST("80810", HttpStatus.CONFLICT,"Source name {} exist"),
    SOURCE_NOT_FOUND("80811", HttpStatus.NOT_FOUND, "Source with id {} not found"),
    TOPIC_NAME_EXIST("80812",HttpStatus.CONFLICT, "Topic name {} exist"),
    TOPIC_NOT_FOUND("80813", HttpStatus.NOT_FOUND, "Topic with id {} not found"),
    NEWS_NOT_FOUND("80814", HttpStatus.NOT_FOUND, "News with id {} not found"),

    // favorite
    FAVORITE_NOT_FOUND("80833", HttpStatus.NOT_FOUND, "Favorite news not found with id {}"),

    // history
    HISTORY_NOT_FOUND("80844", HttpStatus.NOT_FOUND, "History news not found with id {}"),

    // subscription
    FEATURE_NOT_FOUND("80812", HttpStatus.NOT_FOUND, "Feature not found with id {}"),
    FEATURE_CODE_EXIST("80813", HttpStatus.CONFLICT, "Feature code {} exist"),

    PLAN_NOT_FOUND("80814", HttpStatus.NOT_FOUND, "Plan not found with id {}"),

    // payment
    PAYMENT_NOT_FOUND("80214", HttpStatus.NOT_FOUND, "payment not found with id {}"),

    // access denice
    ACCESS_DENICE("1000", HttpStatus.FORBIDDEN, "access denice!")
    ;

    private final String code;
    private final HttpStatus status;
    private final String messageTemplate;

    ErrorCode(String code, HttpStatus status, String messageTemplate) {
        this.code=code;
        this.status = status;
        this.messageTemplate = messageTemplate;
    }

    public String formatMessage(Object... args) {
        return String.format(messageTemplate.replace("{}", "%s"), args);
    }
}
