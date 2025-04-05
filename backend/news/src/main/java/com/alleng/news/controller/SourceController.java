package com.alleng.news.controller;

import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.news.payload.request.SourceVM;
import com.alleng.news.payload.response.SourceMV;
import com.alleng.news.service.ISourceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1/source}/source")
public class SourceController {

    ISourceService sourceService;

    @GetMapping("/all")
    public ResponseEntity<ApiVM<List<SourceMV>>> getAllSource() {
        List<SourceMV> mvList = sourceService.getAll();
        ApiVM<List<SourceMV>> apiVM = new ApiVM<>(mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("WRITE_SOURCE")
    @PostMapping("")
    public ResponseEntity<ApiVM<SourceMV>> createSource(@RequestBody SourceVM sourceVM) {
        SourceMV sourceMV = sourceService.createSource(sourceVM);
        ApiVM<SourceMV> apiVM = new ApiVM<>(sourceMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("WRITE_SOURCE")
    @PutMapping("/{sourceId}")
    public ResponseEntity<ApiVM<SourceMV>> updateSource(@PathVariable(name = "sourceId") UUID sourceId, @RequestBody SourceVM sourceVM) {
        SourceMV sourceMV = sourceService.updateSource(sourceId, sourceVM);
        ApiVM<SourceMV> apiVM = new ApiVM<>(sourceMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("WRITE_SOURCE")
    @DeleteMapping("")
    public ResponseEntity<ApiVM<List<UUID>>> deleteSources(@RequestBody List<UUID> sourceIdList) {
        List<UUID> list = sourceService.deleteSource(sourceIdList);
        ApiVM<List<UUID>> apiVM = new ApiVM<>(list);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
