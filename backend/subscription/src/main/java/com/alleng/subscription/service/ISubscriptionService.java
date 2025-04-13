package com.alleng.subscription.service;

import com.alleng.subscription.constant.CodeType;
import com.alleng.subscription.payload.request.SubscriptionVM;
import com.alleng.subscription.payload.response.SubscriptionMV;
import org.springframework.security.oauth2.jwt.Jwt;

public interface ISubscriptionService {
    SubscriptionMV createSubscription(SubscriptionVM vm, String subject);

    Boolean hasAccessToFeature(CodeType code, String subject);
}
