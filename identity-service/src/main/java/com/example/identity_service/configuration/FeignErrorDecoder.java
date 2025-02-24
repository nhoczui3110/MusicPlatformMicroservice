//package com.example.identity_service.configuration;
//
//import com.example.identity_service.dto.request.ApiResponse;
//import com.example.identity_service.exception.AppException;
//import com.example.identity_service.exception.ErrorCode;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Slf4j
//public class FeignErrorDecoder implements ErrorDecoder {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        log.error("Feign error: {} - {}", response.status(), response.reason());
//
//        try {
//            ApiResponse apiResponse = objectMapper.readValue(response.body().asInputStream(), ApiResponse.class);
//            return new AppException(ErrorCode.UNCATEGORIZED, apiResponse.getMessage());
//        } catch (IOException e) {
//            log.error("Error parsing Feign response", e);
//            return new AppException(ErrorCode.UNCATEGORIZED, "Error processing request");
//        }
//    }
//}
