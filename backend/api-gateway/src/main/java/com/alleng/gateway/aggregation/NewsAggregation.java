package com.alleng.gateway.aggregation;

import com.alleng.gateway.feign.NewsClient;
import com.alleng.gateway.payload.response.ApiVM;
import com.alleng.gateway.payload.response.NewsMV;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/aggregate/news")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NewsAggregation {

    NewsClient newsClient;

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiVM<Map<String, Object>>> getNewsAndRandomList(@PathVariable(name = "newsId") UUID newsId) {

        NewsMV newsMV = Objects.requireNonNull(newsClient.getNews(newsId).getBody()).data();

        List<NewsMV> mvList = Objects.requireNonNull(newsClient.getListRand().getBody()).data();

        Map<String, Object> map = Map.of(
                "news", newsMV,
                "list", mvList
        );
        ApiVM<Map<String, Object>> apiVM = new ApiVM<>(map);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }


}
