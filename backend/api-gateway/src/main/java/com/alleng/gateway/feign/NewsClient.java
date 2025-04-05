package com.alleng.gateway.feign;

import com.alleng.gateway.config.FeignConfig;
import com.alleng.gateway.payload.response.ApiVM;
import com.alleng.gateway.payload.response.NewsMV;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "news",
        url = "http://localhost:8081/api/v1/news/",
        configuration = FeignConfig.class
)
public interface NewsClient {

    @GetMapping("{newsId}")
    ResponseEntity<ApiVM<NewsMV>> getNews(@PathVariable(name = "newsId") UUID newsId);

    @GetMapping("list/rand")
    ResponseEntity<ApiVM<List<NewsMV>>> getListRand();
}
