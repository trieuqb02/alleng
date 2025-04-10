package com.alleng.gateway.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private String errorCode;
    private Date timestamp;
    private String message;

    @Setter
    private int status;

    public CustomException(String errorCode, Date timestamp, String message) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = timestamp;
        this.message = message;
    }

}
