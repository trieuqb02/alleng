package com.alleng.news.service;

import com.alleng.news.payload.request.SourceVM;
import com.alleng.news.payload.response.SourceMV;

import java.util.List;
import java.util.UUID;

public interface ISourceService {

    List<SourceMV> getAll();

    SourceMV createSource(SourceVM sourceVM);

    SourceMV updateSource(UUID sourceId, SourceVM sourceVM);

    List<UUID> deleteSource(List<UUID> sourceIdList);
}
