package com.MusicPlatForm.user_library_service.controller;

import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.album.AlbumResponse;
import com.MusicPlatForm.user_library_service.service.AlbumService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/album")
public class AlbumController {
    AlbumService  albumService;



    @GetMapping("/find-by-album-id/{albumId}")
    public ApiResponse<AlbumResponse> getAlbum(@PathVariable("albumId") String albumId) {
        return  ApiResponse.<AlbumResponse>builder().data(albumService.getAlbumById(albumId)).build();
    }

    @GetMapping("/find-by-user-id/{userId}")
    public ApiResponse<Page<AlbumResponse>> getAlbums(@RequestParam("page") int page, @RequestParam("size") int size
            , @PathVariable("userId") String userId){
        return  ApiResponse.<Page<AlbumResponse>>builder().data(albumService.getAlbumByUserId(page, size, userId)).build();
    }

}
