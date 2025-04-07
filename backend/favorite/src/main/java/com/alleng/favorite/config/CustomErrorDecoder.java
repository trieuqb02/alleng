package com.alleng.favorite.config;

import com.alleng.commonlibrary.exception.AccessDeniedException;
import com.alleng.commonlibrary.exception.BadRequestException;
import com.alleng.commonlibrary.exception.CustomException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        CustomException message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, CustomException.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        return switch (response.status()) {
            case 404 -> new NotFoundException(message.getErrorCode());
            case 401 -> new AccessDeniedException(message.getErrorCode());
            default -> new BadRequestException(message.getErrorCode());
        };
    }
}
