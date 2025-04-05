package com.alleng.news.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.BadRequestException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.news.entity.Source;
import com.alleng.news.payload.request.SourceVM;
import com.alleng.news.payload.response.SourceMV;
import com.alleng.news.repository.SourceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SourceService implements ISourceService {

    SourceRepository sourceRepository;

    @Transactional(readOnly = true)
    @Override
    public List<SourceMV> getAll() {
        List<Object[]> results = sourceRepository.findSourcesWithNewsCount();
        return results.stream()
                .map(r -> new SourceMV(
                        (UUID) r[0],
                        (String) r[1],
                        (String) r[2],
                        (Boolean) r[3],
                        (int) ((Number) r[4]).longValue()))
                .toList();
    }

    @Transactional
    @Override
    public SourceMV createSource(SourceVM sourceVM) {
        boolean checkName = sourceRepository.existsByName(sourceVM.name());
        if (checkName) {
            throw new BadRequestException(ErrorCode.SOURCE_NAME_EXIST, sourceVM.name());
        }
        Source source = Source.builder()
                .name(sourceVM.name())
                .description(sourceVM.description())
                .enable(sourceVM.enable())
                .build();

        return SourceMV.convertSourceMV(sourceRepository.save(source));
    }

    @Transactional
    @Override
    public SourceMV updateSource(UUID sourceId, SourceVM sourceVM) {
        Source source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SOURCE_NOT_FOUND, sourceId));

        if (!source.getName().equals(sourceVM.name())) {
            boolean checkName = sourceRepository.existsByName(sourceVM.name());
            if (checkName) {
                throw new BadRequestException(ErrorCode.SOURCE_NAME_EXIST, sourceVM.name());
            }
        }
        source.setName(sourceVM.name());
        source.setDescription(sourceVM.description());
        source.setEnable(sourceVM.enable());
        source = sourceRepository.save(source);
        return SourceMV.convertSourceMV(source);
    }

    @Transactional
    @Override
    public List<UUID> deleteSource(List<UUID> sourceIdList) {
        List<Source> sources = sourceRepository.findAllById(sourceIdList);

        List<Source> sourcesToUpdate = new ArrayList<>();
        List<UUID> sourceIdsToDelete = new ArrayList<>();

        sources.forEach(source -> {
            if (!source.getNews().isEmpty()) {
                source.setEnable(false);
                sourcesToUpdate.add(source);
            } else {
                sourceIdsToDelete.add(source.getId());
            }
        });

        if (!sourcesToUpdate.isEmpty()) {
            sourceRepository.saveAll(sourcesToUpdate);
        }

        if (!sourceIdsToDelete.isEmpty()) {
            sourceRepository.deleteAllById(sourceIdsToDelete);
        }

        return sourceIdsToDelete;
    }
}
