package com.alleng.subscription.service;

import com.alleng.subscription.payload.response.FeatureMV;

import java.util.List;
import java.util.UUID;

public interface IFeatureService {
    List<FeatureMV> getAll();
}
