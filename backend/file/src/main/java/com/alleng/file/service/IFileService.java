package com.alleng.file.service;

import com.alleng.file.payload.MediaConsumer;

import java.io.IOException;

public interface IFileService {
    void createFile(MediaConsumer mediaPL, String authorizationHeader) throws IOException;

    void updateFile(MediaConsumer mediaPL, String authorizationHeader);

    void deleteFile(MediaConsumer mediaPL);
}
