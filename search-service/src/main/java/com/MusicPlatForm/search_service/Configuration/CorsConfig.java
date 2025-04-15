package com.MusicPlatForm.search_service.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // áp dụng cho tất cả các endpoint
                .allowedOrigins("*") // cho phép tất cả origin
                .allowedMethods("*") // cho phép tất cả HTTP methods (GET, POST, etc.)
                .allowedHeaders("*"); // cho phép tất cả headers
    }
}
