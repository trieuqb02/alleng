package com.alleng.history.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.AccessDeniedException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.history.entity.History;
import com.alleng.history.feign.NewsClient;
import com.alleng.history.payload.request.PaginationVM;
import com.alleng.history.payload.response.HistoryMV;
import com.alleng.history.payload.response.HistoryWithNewsMV;
import com.alleng.history.payload.response.NewsMV;
import com.alleng.history.repository.HistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HistoryService implements IHistoryService {

    NewsClient newsClient;

    HistoryRepository historyRepository;

    @Override
    public HistoryMV addHistory(UUID newsId, String subject) {
        History history = History.builder()
                .newsId(newsId)
                .userId(UUID.fromString(subject))
                .readAt(LocalDateTime.now())
                .build();

        history = historyRepository.save(history);
        return new HistoryMV(history.getId(), history.getNewsId(), history.getUserId());
    }

    @Override
    public PaginationMV<HistoryWithNewsMV> getHistories(PaginationVM paginationVM, String subject) {
        Sort sort = paginationVM.sortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(paginationVM.sortBy()).ascending() : Sort.by(paginationVM.sortBy()).descending();

        Pageable pageable = PageRequest.of(paginationVM.page() - 1, paginationVM.limit(), sort);

        Page<History> resultPage = historyRepository.findAllByUserId(UUID.fromString(subject), pageable);

        List<History> histories = resultPage.getContent();

        List<UUID> ids = histories.stream().map(History::getNewsId).toList();

        List<NewsMV> newsMVList = Objects.requireNonNull(newsClient.getList(ids).getBody()).data();

        Map<UUID, NewsMV> newsMap = newsMVList.stream()
                .collect(Collectors.toMap(NewsMV::uuid, Function.identity()));

        List<HistoryWithNewsMV> collect = histories.stream()
                .map(fav -> {
                    NewsMV news = newsMap.get(fav.getNewsId());
                    return new HistoryWithNewsMV(fav.getId(), news);
                })
                .toList();

        return new PaginationMV<>(collect, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements(), resultPage.isLast());
    }

    @Override
    public void deleteHistory(UUID historyId, String subject) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.HISTORY_NOT_FOUND));
        if (!history.getUserId().equals(UUID.fromString(subject))) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENICE);
        }
        historyRepository.delete(history);
    }

    @Override
    public Long countTheTime(String subject) {
        LocalDateTime now = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrow = now.plusDays(1);
        return historyRepository.countByUserIdAndReadAtBetween(UUID.fromString(subject), now, tomorrow);
    }
}
