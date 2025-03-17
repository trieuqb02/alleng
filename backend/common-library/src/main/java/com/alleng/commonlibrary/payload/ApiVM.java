package com.alleng.commonlibrary.payload;

import java.util.ArrayList;
import java.util.Date;

public record ApiVM<T>(String status, String message, T data, Date timestamp) {

    public ApiVM(String status, String message, T data) {
        this(status, message, data, new Date());
    }

    public ApiVM(String status, String message) {
        this(status, message, null, new Date());
    }

}
