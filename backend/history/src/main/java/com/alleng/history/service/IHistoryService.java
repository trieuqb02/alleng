package com.alleng.history.service;

import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.history.payload.request.PaginationVM;
import com.alleng.history.payload.response.HistoryMV;
import com.alleng.history.payload.response.HistoryWithNewsMV;

import java.util.UUID;

public interface IHistoryService {
    HistoryMV addHistory(UUID newsId, String subject);

    PaginationMV<HistoryWithNewsMV> getHistories(PaginationVM paginationVM, String subject);

    void deleteHistory(UUID historyId, String subject);
}
