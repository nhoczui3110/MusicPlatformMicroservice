package com.MusicPlatForm.user_library_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MusicPlatForm.user_library_service.dto.request.playlist.PlaylistTrackRequest;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.service.PlaylistTrackService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/playlist/tracks")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PlaylistTrackController {
    PlaylistTrackService playlistTrackService;
    @PostMapping("/add")
    public ApiResponse<String> addTrackToPlaylist(@Valid @RequestBody PlaylistTrackRequest trackRequest){
        this.playlistTrackService.addTrackToPlaylist(trackRequest);
        return ApiResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .data("Added successfully").build();
    }
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteTrackFromPlaylist(@Valid @RequestBody PlaylistTrackRequest trackRequest){
        this.playlistTrackService.deleteTrackFromPlaylist(trackRequest);
        return ApiResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .data("Deleted successfully").build();
    }
}
