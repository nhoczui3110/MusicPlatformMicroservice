package com.MusicPlatForm.music_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.music_service.dto.reponse.ApiResponse;
import com.MusicPlatForm.music_service.dto.reponse.TrackResponse;
import com.MusicPlatForm.music_service.dto.request.TrackRequest;
import com.MusicPlatForm.music_service.service.implement.TrackService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/track")
@AllArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackController {
    TrackService trackService;
    @GetMapping("/test")
    public void a(){}
    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> addTrack(@RequestPart(name = "cover_image") MultipartFile coverImage,
                                        @RequestPart(name = "track_audio") MultipartFile trackAudio, 
                                        @RequestPart(name = "track") TrackRequest trackRequest){
        this.trackService.uploadTrack(coverImage, trackAudio, trackRequest);
        return ApiResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .data("Added track successfully")
                        .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getTrackById(@PathVariable String id){
        TrackResponse track = this.trackService.getTrackById(id);
        return ApiResponse.<TrackResponse> builder().code(HttpStatus.OK.value()).data(track).build();
    }
}
