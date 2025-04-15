package com.example.identity_service.controller;

import com.example.identity_service.dto.request.*;
import com.example.identity_service.dto.response.AuthenticatedResponse;
import com.example.identity_service.dto.response.UserProfileResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder().data(userService.getUsers( )).build();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

//    @PutMapping("/{userId}")
//    UserResponse updateUser(@RequestBody UserUpdateRequest request, @PathVariable String userId) {
//        return userService.updateUser(userId, request);
//    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "Delete user successfully";
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyinfo() {
        return  ApiResponse.<UserResponse>builder().data(userService.getMyInfo()).build();
    }

//    @PutMapping("/update-my-info")
//    ApiResponse<UserResponse> updateMyInfo(UserUpdateRequest request) {
//        return ApiResponse.<UserResponse>builder().data(userService.updateUser(request)).build();
//    }
//    @PatchMapping("/change-password")
//    ApiResponse changePassword(@RequestBody @Valid ChangePasswordRequest request) {
//        userService.changePassword(request);
//        return  ApiResponse.builder().build();
//    }

}
