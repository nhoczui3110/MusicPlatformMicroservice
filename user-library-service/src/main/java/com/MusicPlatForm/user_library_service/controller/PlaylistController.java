package com.MusicPlatForm.user_library_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.UpdatePlaylistInfoRequest;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.playlist.PlaylistResponse;
import com.MusicPlatForm.user_library_service.dto.response.playlist.PlaylistTypeResponse;
import com.MusicPlatForm.user_library_service.service.PlaylistService;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/playlist")
@AllArgsConstructor
public class PlaylistController {
    private PlaylistService playlistService;

    //Done
    @GetMapping("/all")
    public ApiResponse<List<PlaylistTypeResponse>> getPlaylist(){
       return playlistService.getPlaylists();
    }
    //Done
    @GetMapping("/{id}")
    public ApiResponse<PlaylistResponse> getPlaylistById(@PathVariable String id){
        return this.playlistService.getPlaylistById(id);
    }
    //Done
    @PostMapping("/add")
    public ApiResponse<PlaylistResponse> savePlaylist(@Valid @RequestBody AddPlaylistRequest request){
        PlaylistResponse playlistResponse = playlistService.addPlaylist(request);
        return ApiResponse.<PlaylistResponse>builder()
                    .code(200)
                    .data(playlistResponse)
                    .build();
    }

    //done
    @PutMapping("/update")
    public ApiResponse<PlaylistResponse> updatePlaylistInfo(@RequestPart(name = "image") MultipartFile image, @RequestPart(name = "playlist") UpdatePlaylistInfoRequest updatePlaylistInfoRequest){
        PlaylistResponse playlistResponse =  playlistService.updatePlaylistInfo(image, updatePlaylistInfoRequest);
        return ApiResponse.<PlaylistResponse>builder()
                    .code(200)
                    .data(playlistResponse)
                    .build();
    }

    //done
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteTrackById(@PathVariable String id){
        playlistService.deletePlaylistById(id);
        return ApiResponse.<String>builder()
                    .code(200)
                    .data("Delete successfully")
                    .build();
    }
}
