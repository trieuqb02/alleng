package com.alleng.subscription.payload.response;

import com.alleng.subscription.entity.Plan;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PlanMV(UUID uuid, String name, BigDecimal price, Boolean enable, String description,
                     List<FeatureMV> features, boolean isDelete) {

    public static PlanMV convertPlanMV(Plan plan) {
        List<FeatureMV> features = plan.getFeatures().stream().map(FeatureMV::convertFeatureMV).toList();

        return new PlanMV(plan.getId(), plan.getName(), plan.getPrice(), plan.getEnable(), plan.getDescription(), features, plan.getSubscriptions().isEmpty());
    }

}
