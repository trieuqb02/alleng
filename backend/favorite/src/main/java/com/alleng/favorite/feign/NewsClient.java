package com.alleng.favorite.feign;

import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.favorite.config.FeignConfig;
import com.alleng.favorite.payload.response.NewsMV;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "news",
        configuration = FeignConfig.class
)
public interface NewsClient {

    @GetMapping("/api/v1/news/list/favorite")
    ResponseEntity<ApiVM<List<NewsMV>>> getListFavorite(@RequestParam List<UUID> ids);
}
