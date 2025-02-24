package com.example.identity_service.service;

import com.example.identity_service.dto.request.ChangePasswordRequest;
import com.example.identity_service.dto.request.ProfileCreationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserProfileResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.mapper.UserProfileMapper;
import com.example.identity_service.repository.ProfileClient;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileClient profileClient;
    UserProfileMapper userProfileMapper;
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User newUser = userMapper.toUser(request);
        HashSet<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoles()));
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode((newUser.getPassword())));
        String userId = userRepository.save(newUser).getId();
        ProfileCreationRequest profileCreationRequest = userProfileMapper.toProfileCreationRequest(request);
        LocalDateTime now = LocalDateTime.now();
        profileCreationRequest.setCreatedAt(now);
        profileCreationRequest.setUpdateAt(now);
        profileCreationRequest.setUserId(userId);
        profileClient.createProfile(profileCreationRequest);
        UserResponse result = userMapper.toUserResponse(newUser);
        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In getUsers method");

        // Lấy Authentication object từ SecurityContext
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // In ra principal và authorities
        log.info("Principal: {}", authentication.getPrincipal());
        log.info("Authorities:");
        authentication.getAuthorities().forEach(authority -> log.info(authority.getAuthority()));

        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.getUsername() == authentication.name")
    public UserResponse getUser(String id) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current username: {}", authentication.getName());
        var res = userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
        log.info("Current res: {}", res.getUsername());
        return res;
    }

//    public UserResponse updateUser(UserUpdateRequest request) {
//        var context = SecurityContextHolder.getContext();
//        String userId = context.getAuthentication().getName();
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        userMapper.updateUser(user, request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));
//        return userMapper.toUserResponse(userRepository.save(user));
//    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException((ErrorCode.USER_NOT_EXISTED)));
        return  userMapper.toUserResponse(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }


    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        log.info("user roles: {}", roles);
        String userId = authentication.getName();

        if (!Objects.equals(userId, request.getUserId()) && !roles.contains("ROLE_ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        if (!Objects.equals(request.getNewPassword(), request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
