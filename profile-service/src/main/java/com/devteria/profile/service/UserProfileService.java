package com.devteria.profile.service;

import com.devteria.profile.dto.request.ApiResponse;
import com.devteria.profile.dto.request.DeleteAvatarRequest;
import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.request.ProfileUpdateRequest;
import com.devteria.profile.dto.response.UploadAvatarResponse;
import com.devteria.profile.dto.response.UploadCoverResponse;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.exception.AppException;
import com.devteria.profile.exception.ErrorCode;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.FileClient;
import com.devteria.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    FileClient fileClient;
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
    public ApiResponse<UploadAvatarResponse> uploadAvatar(MultipartFile avatar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        String oldAvatar = userProfile.getAvatar();
        ApiResponse<UploadAvatarResponse> response;
        if (oldAvatar != null) {
            response = fileClient.replaceAvatar(avatar, oldAvatar);
        } else {
            response = fileClient.addAvatar(avatar);
        }
        userProfile.setAvatar(response.getData().getAvatarName());
        userProfileRepository.save(userProfile);
        return response;
    }

    public ApiResponse<UploadCoverResponse> uploadCover(MultipartFile cover) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        String oldCover = userProfile.getCover();
        ApiResponse<UploadCoverResponse> response;
        if (oldCover != null) {
            response = fileClient.replaceCover(cover, oldCover);
        } else {
            response = fileClient.addCover(cover);
        }
        userProfile.setCover(response.getData().getCoverName());
        userProfileRepository.save(userProfile);
        return response;
    }

}
