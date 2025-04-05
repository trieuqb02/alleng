package com.alleng.news.payload.request;

public record PaginationVM(int page, int limit, String sortDir, String sortBy, String topic, String source,
                           String search,
                           String date) {
}
