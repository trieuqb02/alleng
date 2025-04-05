package com.alleng.news.payload.response;

import java.util.Collection;

public record PaginationMV<T>(Collection<T> content, int pageNumber, int pageSize, int totalPage, long totalElement,
                              boolean isLastPage) {
}
