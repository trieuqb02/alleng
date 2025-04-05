package com.alleng.news.controller;

import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.news.payload.request.TopicVM;
import com.alleng.news.payload.response.TopicMV;
import com.alleng.news.service.ITopicService;
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
@RequestMapping("${com.alleng.prefix.api:/api/v1/topic}/topic")
public class TopicController {

    ITopicService topicService;

    @GetMapping("/all")
    public ResponseEntity<ApiVM<List<TopicMV>>> getAll() {
        List<TopicMV> mvList = topicService.getAllTopic();
        ApiVM<List<TopicMV>> apiVM = new ApiVM<>(mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("WRITE_TOPIC")
    @PostMapping("")
    public ResponseEntity<ApiVM<TopicMV>> createTopic(@RequestBody TopicVM topicVM) {
        TopicMV topicMV = topicService.createTopic(topicVM);
        ApiVM<TopicMV> apiVM = new ApiVM<>(topicMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("UPDATE_TOPIC")
    @PutMapping("/{topicId}")
    public ResponseEntity<ApiVM<TopicMV>> updateTopic(@PathVariable(name = "topicId") UUID topicId, @RequestBody TopicVM topicVM) {
        TopicMV topicMV = topicService.updateTopic(topicId, topicVM);
        ApiVM<TopicMV> apiVM = new ApiVM<>(topicMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("DELETE_TOPIC")
    @DeleteMapping("")
    public ResponseEntity<ApiVM<List<UUID>>> updateTopic(@RequestBody List<UUID> topicIdList) {
        List<UUID> list = topicService.deleteTopic(topicIdList);
        ApiVM<List<UUID>> apiVM = new ApiVM<>(list);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}
