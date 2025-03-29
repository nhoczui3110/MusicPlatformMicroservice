package com.MusicPlatForm.user_library_service.controller;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AlbumRequest;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.album.AlbumResponse;
import com.MusicPlatForm.user_library_service.service.AlbumService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth/album")
public class AuthAlbumController {
    AlbumService  albumService;

    @PostMapping(path = "/add-album", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<AlbumResponse> addAlbum(@Valid @RequestPart("meta-data") AlbumRequest request, @RequestPart(value = "cover-album", required = false) MultipartFile coverAlbum) {
        return  ApiResponse.<AlbumResponse>builder().data(albumService.addAlbum(request, coverAlbum)).build();
    }

    @DeleteMapping(path = "/{albumId}")
    public ApiResponse<String> deleteAlbum(@PathVariable("albumId") String albumId) {
        albumService.deleteAlbumById(albumId);
        return  ApiResponse.<String>builder().data("Delete album successfully!").build();
    }

    @PutMapping(path = "/{albumId}")
    public ApiResponse<AlbumResponse> updateAlbum(@PathVariable("albumId") String albumId,
                                                  @Valid @RequestPart("meta-data") AlbumRequest request,
                                                  @RequestPart(value = "cover-album", required = false) MultipartFile coverAlbum) {
        return ApiResponse.<AlbumResponse>builder().data(albumService.editAlbum(request, coverAlbum, albumId)).build();
    }
}
