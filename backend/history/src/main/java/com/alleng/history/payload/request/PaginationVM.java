package com.alleng.history.payload.request;

public record PaginationVM(int page, int limit, String sortDir, String sortBy) {
}
