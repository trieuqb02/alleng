package com.alleng.subscription.payload.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PlanVM(String name, BigDecimal price, Boolean enable, String description,int durationDay, List<UUID> features) {
}
