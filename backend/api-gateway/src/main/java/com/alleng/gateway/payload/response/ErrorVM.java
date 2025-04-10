package com.alleng.gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorVM(String errorCode, String message, List<FieldError> fieldErrors, Date timestamp) {

    public record FieldError(String field, String message) {
    }

    public ErrorVM(String status, String message, List<FieldError> errors) {
        this(status, message, errors, new Date());
    }

    public ErrorVM(String status, String message) {
        this(status, message, null, new Date());
    }
}
