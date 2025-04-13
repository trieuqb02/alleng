package com.alleng.subscription.service;

import com.alleng.subscription.payload.response.FeatureMV;
import com.alleng.subscription.repository.FeatureRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeatureService implements IFeatureService {

    FeatureRepository featureRepository;

    @Transactional(readOnly = true)
    @Override
    public List<FeatureMV> getAll() {
        return featureRepository.findAll().stream().map(FeatureMV::convertFeatureMV).collect(Collectors.toList());
    }

}
