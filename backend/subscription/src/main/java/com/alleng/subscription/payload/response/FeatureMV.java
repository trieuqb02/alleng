package com.alleng.subscription.payload.response;

import com.alleng.subscription.constant.CodeType;
import com.alleng.subscription.entity.Feature;

import java.util.UUID;

public record FeatureMV(UUID id, CodeType code, String name, String description) {

    public static FeatureMV convertFeatureMV(Feature feature) {
        return new FeatureMV(feature.getId(), feature.getCode(), feature.getName(), feature.getDescription());
    }
}
