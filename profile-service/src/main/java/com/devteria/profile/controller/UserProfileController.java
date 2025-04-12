package com.devteria.profile.controller;

import com.devteria.profile.dto.request.ApiResponse;
import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.request.ProfileUpdateRequest;
import com.devteria.profile.dto.response.ProfileWithCountFollowResponse;
import com.devteria.profile.dto.response.UploadAvatarResponse;
import com.devteria.profile.dto.response.UploadCoverResponse;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserProfileController {
    UserProfileService userProfileService;
    @GetMapping("/{userId}")
    ApiResponse<ProfileWithCountFollowResponse> getUserProfile(@PathVariable("userId") String userId) {
        return ApiResponse.<ProfileWithCountFollowResponse>builder()
                .data(userProfileService.get(userId))
                .build();
    }
    @GetMapping("/email/{email}")
    ApiResponse<UserProfileResponse> getUserProfileByEmail(@PathVariable("email") String email) {
        UserProfileResponse response = userProfileService.getByEmail(email);
        return ApiResponse.<UserProfileResponse>builder()
                .data(response)
                .build();
    }

//    @GetMapping("/avatar/{fileName}")
//    public ResponseEntity<Resource> getProfileAvatar(@PathVariable String fileName) {
//        Resource file = userProfileService.getProfileAvatar(fileName);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                .body(file);
//    }

}
