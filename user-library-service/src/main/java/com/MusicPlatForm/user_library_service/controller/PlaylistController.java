package com.MusicPlatForm.user_library_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.UpdatePlaylistInfoRequest;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.playlist.PlaylistResponse;
import com.MusicPlatForm.user_library_service.entity.Playlist;
import com.MusicPlatForm.user_library_service.service.PlaylistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/playlist")
@AllArgsConstructor
public class PlaylistController {
    private PlaylistService playlistService;

    @GetMapping("/all")
    public ApiResponse<List<PlaylistResponse>> getPlaylist(){
       return playlistService.getPlaylists();
    }

    @GetMapping("/{id}")
    public ApiResponse<PlaylistResponse> getPlaylistById(@PathVariable String id){
        return this.playlistService.getPlaylistById(id);
    }

    @PostMapping("/add")
    public ApiResponse<String> savePlaylist(@Valid @RequestBody AddPlaylistRequest request){
        playlistService.addPlaylist(request);
        return ApiResponse.<String>builder()
                    .code(200)
                    .data("Saved successfully")
                    .build();
    }

    @PutMapping("/update")
    public ApiResponse<String> updatePlaylistInfo(@Valid @RequestBody UpdatePlaylistInfoRequest request) {
        playlistService.updatePlaylistInfo(request);
        return ApiResponse.<String>builder()
                    .code(200)
                    .data("Updated successfully")
                    .build();
    }
    // @PutMapping("/update")
    // public ApiResponse<String> updatePlaylistInfo(@RequestPart MultipartFile image, @RequestParam(name = "Playlist") String playlistJson) throws JsonMappingException, JsonProcessingException{
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     UpdatePlaylistInfoRequest request =objectMapper.readValue(playlistJson,UpdatePlaylistInfoRequest.class);

    //     return ApiResponse.<String>builder()
    //                 .code(200)
    //                 .data("Updated successfully")
    //                 .build();
    // }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteTrackById(@PathVariable String id){
        playlistService.deletePlaylistById(id);
        return ApiResponse.<String>builder()
                    .code(200)
                    .data("Delete successfully")
                    .build();
    }
}
