package com.alleng.gateway.payload.response;

import java.util.Date;

public record ApiVM<T>(String message, T data, Date timestamp) {

    public ApiVM(String message, T data) {
        this(message, data, new Date());
    }

    public ApiVM(T data) {
        this(null, data, new Date());
    }

}
