package com.alleng.subscription.controller;

import com.alleng.commonlibrary.constant.PaginationConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.subscription.payload.request.PlanVM;
import com.alleng.subscription.payload.response.PlanMV;
import com.alleng.subscription.service.IPlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1}/plan")
public class PlanController {

    IPlanService planService;

    @GetMapping("")
    public ResponseEntity<ApiVM<PaginationMV<PlanMV>>> getPlanList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_ID) String sortBy
    ) {
        PaginationMV<PlanMV> paginationMV = planService.getPlanList(page, limit, sortDir, sortBy);
        ApiVM<PaginationMV<PlanMV>> apiVM = new ApiVM<>(paginationMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PostMapping("")
    ResponseEntity<ApiVM<PlanMV>> createPlan(@RequestBody PlanVM vm) {
        PlanMV mv = planService.createPlan(vm);
        ApiVM<PlanMV> apiVM = new ApiVM<>(mv);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<ApiVM<PlanMV>> updatePlan(
            @PathVariable("planId") UUID planId,
            @RequestBody PlanVM vm) {
        PlanMV mv = planService.updatePlan(planId, vm);
        ApiVM<PlanMV> apiVM = new ApiVM<>(mv);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<ApiVM<UUID>> deletePlan(@PathVariable("planId") UUID planId) {
        planService.deletePlan(planId);
        ApiVM<UUID> apiVM = new ApiVM<>(planId);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

}
