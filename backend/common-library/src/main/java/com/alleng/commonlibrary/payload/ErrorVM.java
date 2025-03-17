package com.alleng.commonlibrary.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorVM(String status, String message, List<Error> errors, Date timestamp) {

    public record Error(String field, String message){ }

    public ErrorVM(String status, String message, List<Error> errors) {
        this(status, message, errors, new Date());
    }
}
