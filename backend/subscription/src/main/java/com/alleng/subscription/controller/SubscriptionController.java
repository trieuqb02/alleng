package com.alleng.subscription.controller;

import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.subscription.constant.CodeType;
import com.alleng.subscription.constant.CurrentUser;
import com.alleng.subscription.payload.request.SubscriptionVM;
import com.alleng.subscription.payload.response.SubscriptionMV;
import com.alleng.subscription.service.ISubscriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1}/subscription")
public class SubscriptionController {

    ISubscriptionService subscriptionService;

    @PostMapping("")
    ResponseEntity<ApiVM<SubscriptionMV>> createSubscription(@RequestBody SubscriptionVM vm, @CurrentUser Jwt jwt) {
        SubscriptionMV mv = subscriptionService.createSubscription(vm, jwt.getSubject());
        ApiVM<SubscriptionMV> apiVM = new ApiVM<>(mv);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @GetMapping("/feature")
    ResponseEntity<Boolean> hasAccessToFeature(@CurrentUser Jwt jwt, @RequestParam("code") CodeType code) {
        Boolean check = subscriptionService.hasAccessToFeature(code, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(check);
    }

}
