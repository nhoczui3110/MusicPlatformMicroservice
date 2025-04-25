package com.MusicPlatForm.user_library_service.httpclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.ProfileWithCountFollowResponse;

@FeignClient(name = "profile-service")
public interface ProfileClient {
  @GetMapping("/bulk")
  public ApiResponse<List<ProfileWithCountFollowResponse>> getUserProfileByIds(@RequestParam List<String> userIds);
}
