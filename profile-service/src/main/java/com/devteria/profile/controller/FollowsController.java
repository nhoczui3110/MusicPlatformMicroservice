package com.devteria.profile.controller;

import com.devteria.profile.dto.request.AddFollowRequest;
import com.devteria.profile.dto.request.ApiResponse;
import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.Follows;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.service.FollowsService;
import com.devteria.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/follows")
public class FollowsController {
    FollowsService followsService;

    @GetMapping("/get-followers/{userId}")
    public ApiResponse<Page<UserProfileResponse>> getFollowers(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @PathVariable("userId") String userId) {
        return ApiResponse.<Page<UserProfileResponse>>builder().data(followsService.getFollowers(page, size, userId)).build();
    }

    @GetMapping("/get-followings/{userId}")
    public ApiResponse<Page<UserProfileResponse>> getFollowings(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @PathVariable("userId") String userId) {
        return ApiResponse.<Page<UserProfileResponse>>builder().data(followsService.getFollowing(page, size, userId)).build();
    }


}
