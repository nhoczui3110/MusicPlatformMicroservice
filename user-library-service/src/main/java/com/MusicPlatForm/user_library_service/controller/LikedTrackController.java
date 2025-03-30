package com.MusicPlatForm.user_library_service.controller;

import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import com.MusicPlatForm.user_library_service.dto.response.liketrack.LikedTrackResponse;
import com.MusicPlatForm.user_library_service.service.LikedTrackService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liked")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikedTrackController {
    LikedTrackService likedTrackService;

    @GetMapping("/all")
    public ApiResponse<List<TrackResponse>> getAllLikedTracks() {
        List<TrackResponse> likedTracks = likedTrackService.getAllLikedTracks();
        return ApiResponse.<List<TrackResponse>>builder().data(likedTracks).code(200).build();
    }

    @GetMapping("/is_liked/{trackId}")
    public ApiResponse<Boolean> isLiked(@PathVariable String trackId) {
        return ApiResponse.<Boolean>builder().data(likedTrackService.isLiked(trackId)).code(200).build();
    }

    @PostMapping("/like/{trackId}")
    public ApiResponse<Boolean> likeTrack(@PathVariable String trackId) {
        return ApiResponse.<Boolean>builder().data(likedTrackService.likeTrack(trackId)).code(200).build();
    }

    @PostMapping("/unlike/{id}")
    public ApiResponse<Boolean> unLikeTrack(@PathVariable String id) {
        return ApiResponse.<Boolean>builder().data(likedTrackService.unLikeTrack(id)).code(200).build();
    }
}
