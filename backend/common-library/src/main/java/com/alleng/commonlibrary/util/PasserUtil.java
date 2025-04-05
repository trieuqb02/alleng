package com.alleng.commonlibrary.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PasserUtil {

    ObjectMapper objectMapper;

    public String parseToJson(Object event) {
        try {
            return new ObjectMapper().writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing event to JSON", e);
        }
    }

    public <T> T parseToObject(String eventJson, Class<T> eventType) {
        try {
            return objectMapper.readValue(eventJson, eventType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid event data for " + eventType.getSimpleName());
        }
    }

    public <T> T parseToObject(byte[] eventJson, Class<T> eventType) {
        try {
            return objectMapper.readValue(eventJson, eventType);
        } catch (IOException e) {
            throw new RuntimeException("Invalid event data for " + eventType.getSimpleName());
        }
    }

    public String convertByteToBase64(byte[] file) {
        return Base64.getEncoder().encodeToString(file);
    }

    public byte[] convertBase64ToBytes(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

}
