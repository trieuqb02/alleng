package com.alleng.commonlibrary.payload;

import java.util.Collection;

public record PaginationMV<T>(Collection<T> content, int pageNumber, int pageSize, int totalPage, long totalElement,
                              boolean isLastPage) {
}
