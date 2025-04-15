package com.MusicPlatForm.file_service.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
        // Cấu hình CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8888"));  // Chỉ định nguồn (origin) được phép
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Các phương thức HTTP được phép
        configuration.setAllowedHeaders(List.of("*"));  // Cho phép tất cả các headers
        configuration.setAllowCredentials(true);  // Cho phép gửi cookies và authentication token
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Áp dụng CORS cho tất cả các endpoint
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors->{})  // Kích hoạt CORS
            .authorizeHttpRequests(request -> request
                .requestMatchers("/**").permitAll()  // Cho phép tất cả các yêu cầu
                .anyRequest().authenticated()  // Yêu cầu xác thực cho các yêu cầu khác
            )
            .csrf(AbstractHttpConfigurer::disable);  // Tắt CSRF
        return http.build();
    }
}
