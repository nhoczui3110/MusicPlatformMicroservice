package com.MusicPlatForm.user_library_service.controller;

import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.album.AlbumResponse;
import com.MusicPlatForm.user_library_service.service.AlbumService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/album")
public class AlbumController {
    AlbumService  albumService;



    @GetMapping("/find-by-album-id/{albumId}")
    public ApiResponse<AlbumResponse> getAlbumById(@PathVariable("albumId") String albumId) {
        return  ApiResponse.<AlbumResponse>builder().data(albumService.getAlbumById(albumId)).build();
    }
    @GetMapping("/{albumLink}")
    public ApiResponse<AlbumResponse> getAlbumByAlbumLink(@PathVariable("albumLink") String albumLink) {
        return  ApiResponse.<AlbumResponse>builder().data(albumService.getAlbumByLink(albumLink)).build();
    }
    @GetMapping("/find-by-user-id/{userId}")
    public ApiResponse<List<AlbumResponse>> getAlbums(@PathVariable("userId") String userId){
        return  ApiResponse.<List<AlbumResponse>>builder().data(albumService.getAlbumByUserId(userId)).build();
    }
    @GetMapping("/liked-albums/{userId}")
    public ApiResponse<List<AlbumResponse>> getLikedAlbums(@PathVariable("userId") String userId) {
        return  ApiResponse.<List<AlbumResponse>>builder().data(albumService.getLikedAlbums(userId)).build();
    }

    @GetMapping("/get-liked-created-album/{userId}")
    public ApiResponse<List<AlbumResponse>> getAll(@PathVariable("userId") String userId) {
        return ApiResponse.<List<AlbumResponse>>builder().data(albumService.getCreatedAndLikedAlbum(userId)).build();
    }
}
