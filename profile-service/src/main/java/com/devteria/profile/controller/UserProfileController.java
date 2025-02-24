package com.devteria.profile.controller;

import com.devteria.profile.dto.request.ApiResponse;
import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.request.ProfileUpdateRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserProfileController {
    UserProfileService userProfileService;
    @GetMapping("/{userId}")
    ApiResponse<UserProfileResponse> getUserProfile(@PathVariable("userId") String userId) {
        return ApiResponse.<UserProfileResponse>builder()
                .data(userProfileService.get(userId))
                .build();
    }
    @GetMapping()
    ApiResponse<Page<UserProfileResponse>> getAllUserProfile(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<UserProfileResponse>>builder()
                .data(userProfileService.getAll(page, size)).build();
    }

    @PutMapping("update-my-info")
    ApiResponse<UserProfileResponse> updateUserProfile(@RequestBody ProfileUpdateRequest request) {
        return  ApiResponse.<UserProfileResponse>builder().data(userProfileService.update(request)).build();
    }
    @GetMapping("get-my-info")
    ApiResponse<UserProfileResponse> getMyInfo() {
        return ApiResponse.<UserProfileResponse>builder().data(userProfileService.getMyInfo()).build();
    }
}
