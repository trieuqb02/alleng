package com.alleng.gateway.aggregation;

import com.alleng.gateway.feign.HistoryClient;
import com.alleng.gateway.feign.NewsClient;
import com.alleng.gateway.payload.response.ApiVM;
import com.alleng.gateway.payload.response.NewsMV;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/aggregate/news")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NewsAggregation {

    public final static int EXPIRED_LIMITING = 3;

    NewsClient newsClient;

    HistoryClient historyClient;

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiVM<Map<String, Object>>> getNewsAndRandomList(
            @PathVariable UUID newsId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "viewCount", defaultValue = "0") int viewCount,
            @CookieValue(value = "viewDate", defaultValue = "0") String viewDate,
            ServerWebExchange exchange
    ) {
        String message = "";
        Map<String, Object> data = null;

        // Check authorization-based reading limit
        if (authHeader != null) {
            Long readCount = Objects.requireNonNull(historyClient.countTheTime(authHeader).getBody()).data();
            if (readCount >= EXPIRED_LIMITING) {
                message = "The daily reading limit has expired!";
                return buildResponse(exchange, message, data, viewCount, viewDate);
            }
        }

        // Reset daily view count if the date has changed
        String today = LocalDate.now().toString();
        if (!today.equals(viewDate)) {
            viewDate = today;
            viewCount = 0;
        }

        // Check cookie-based reading limit
        if (viewCount >= EXPIRED_LIMITING) {
            message = "The daily reading limit has expired!";
        } else {
            NewsMV news = Objects.requireNonNull(newsClient.getNews(newsId).getBody()).data();
            List<NewsMV> randomList = Objects.requireNonNull(newsClient.getListRand().getBody()).data();

            data = Map.of(
                    "news", news,
                    "list", randomList
            );
            viewCount++; // Increment after successful view
        }

        return buildResponse(exchange, message, data, viewCount, viewDate);
    }

    private ResponseEntity<ApiVM<Map<String, Object>>> buildResponse(
            ServerWebExchange exchange,
            String message,
            Map<String, Object> data,
            int viewCount,
            String viewDate
    ) {
        // Set up a cookie-based reading limit
        ServerHttpResponse response = exchange.getResponse();
        response.addCookie(ResponseCookie.from("viewCount", String.valueOf(viewCount))
                .maxAge(Duration.ofDays(1))
                .path("/")
                .httpOnly(true)
                .build());

        response.addCookie(ResponseCookie.from("viewDate", viewDate)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .httpOnly(true)
                .build());

        ApiVM<Map<String, Object>> apiVM = new ApiVM<>(message, data);
        return ResponseEntity.ok(apiVM);
    }
}
