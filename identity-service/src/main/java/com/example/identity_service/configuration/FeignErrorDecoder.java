package com.example.identity_service.configuration;

import com.example.identity_service.dto.request.ApiResponse;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        Map<String, Object> responseBodyMap = null;

        if (response.body() != null) {
            try {
                responseBodyMap = objectMapper.readValue(response.body().asInputStream(), Map.class);
                Integer code = (Integer) responseBodyMap.get("code");
//                Email existed
                if (code == 1014) {
                    return new AppException(ErrorCode.EMAIL_EXISTED);
                }
            } catch (IOException e) {
                log.error("❌ Error parsing Feign response body", e);
            }
        }

        log.error("❌ Feign Error: Status {} - Body: {}", response.status(), responseBodyMap);

        return new RuntimeException("Feign API Error: " + response.status() + " - " + responseBodyMap);
    }
}
