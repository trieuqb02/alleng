package com.alleng.subscription.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.subscription.entity.Feature;
import com.alleng.subscription.entity.Plan;
import com.alleng.subscription.payload.request.PlanVM;
import com.alleng.subscription.payload.response.PlanMV;
import com.alleng.subscription.repository.FeatureRepository;
import com.alleng.subscription.repository.PlanRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PlanService implements IPlanService {

    PlanRepository planRepository;

    FeatureRepository featureRepository;

    @Transactional
    @Override
    public PlanMV createPlan(PlanVM vm) {

        Plan plan = Plan.builder()
                .name(vm.name())
                .price(vm.price())
                .description(vm.description())
                .enable(vm.enable())
                .durationDay(vm.durationDay())
                .subscriptions(List.of())
                .build();

        plan = planRepository.save(plan);

        Set<Feature> features = new HashSet<>(featureRepository.findAllById(vm.features()));

        plan.setFeatures(features);

        return PlanMV.convertPlanMV(planRepository.save(plan));
    }

    @Transactional
    @Override
    public PlanMV updatePlan(UUID planId, PlanVM vm) {

        Plan plan = planRepository.findById(planId).orElseThrow(() -> new NotFoundException(ErrorCode.PLAN_NOT_FOUND));

        plan.setName(vm.name());
        plan.setPrice(vm.price());
        plan.setDurationDay(vm.durationDay());
        plan.setEnable(vm.enable());
        plan.setDescription(vm.description());

        Set<Feature> features = new HashSet<>(featureRepository.findAllById(vm.features()));
        System.out.println("123:" + features.size());
        plan.setFeatures(features);

        return PlanMV.convertPlanMV(planRepository.save(plan));
    }

    @Transactional
    @Override
    public void deletePlan(UUID planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new NotFoundException(ErrorCode.PLAN_NOT_FOUND));
        if (plan.getSubscriptions().isEmpty()) {
            planRepository.delete(plan);
        }
    }

    @Override
    public PaginationMV<PlanMV> getPlanList(int page, int limit, String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Page<Plan> resultPage = planRepository.findAll(pageable);

        List<PlanMV> collect = resultPage.getContent().stream().map(PlanMV::convertPlanMV).collect(Collectors.toList());

        return new PaginationMV<>(collect, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements(), resultPage.isLast());
    }
}
