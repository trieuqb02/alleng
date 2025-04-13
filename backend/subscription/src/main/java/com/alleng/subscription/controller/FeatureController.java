package com.alleng.subscription.controller;

import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.subscription.payload.response.FeatureMV;
import com.alleng.subscription.service.IFeatureService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1}/feature")
public class FeatureController {

    IFeatureService featureService;

    @GetMapping("/all")
    public ResponseEntity<ApiVM<List<FeatureMV>>> getAll() {
        List<FeatureMV> mvList = featureService.getAll();
        ApiVM<List<FeatureMV>> apiVM = new ApiVM<>(mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
