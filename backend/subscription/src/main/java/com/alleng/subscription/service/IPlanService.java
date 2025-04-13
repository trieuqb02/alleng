package com.alleng.subscription.service;

import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.subscription.payload.request.PlanVM;
import com.alleng.subscription.payload.response.PlanMV;

import java.util.UUID;

public interface IPlanService {
    PlanMV createPlan(PlanVM vm);

    PlanMV updatePlan(UUID planId, PlanVM vm);

    void deletePlan(UUID planId);

    PaginationMV<PlanMV> getPlanList(int page, int limit, String sortDir, String sortBy);
}
