package com.devteria.profile.service;

import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.request.ProfileUpdateRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.exception.AppException;
import com.devteria.profile.exception.ErrorCode;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    public UserProfileResponse create(ProfileCreationRequest request) {
        String email = request.getEmail();
        if (userProfileRepository.findByEmail(email).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        UserProfile userProfile =  userProfileRepository.save(userProfileMapper.toUserProfile(request));
        return  userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse update(ProfileUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = "";
        if (authentication != null) {
            id = authentication.getName();
        }
        UserProfile userProfile = userProfileRepository.findByUserId(id).orElseThrow(() ->new AppException(ErrorCode.PROFILE_NOT_FOUND));
        userProfileMapper.updateUserProfile(userProfile, request);
        userProfile.setUpdateAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse get(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("UserProfile is not exist"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserProfileResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfile> userProfiles = userProfileRepository.findAll(pageable);
        return userProfiles.map(userProfileMapper::toUserProfileResponse);
    }
    public UserProfileResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
}
