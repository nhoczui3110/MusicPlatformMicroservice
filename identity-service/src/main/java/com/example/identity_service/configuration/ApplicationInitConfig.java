package com.example.identity_service.configuration;

import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
//    @Bean
//    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
//        return args -> {
//            if (!userRepository.existsByUsername("admin")) {
//                HashSet<Role> roles = new HashSet<>();
//                Role adminRole = roleRepository.findById("ADMIN").orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
//                roles.add(adminRole);
//                User adminUser = User.builder()
//                        .username("admin")
//                        .roles(roles)
//                        .password(passwordEncoder.encode("adminadmin"))
//                        .build();
//                userRepository.save(adminUser);
//                log.warn("Admin user created with password adminadmin");
//            }
//        };
//    }
}
