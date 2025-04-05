package com.alleng.commonlibrary.payload;

import java.util.Date;

public record ApiVM<T>(String message, T data, Date timestamp) {

    public ApiVM(String message, T data) {
        this(message, data, new Date());
    }

    public ApiVM(T data) {
        this(null, data, new Date());
    }

}
