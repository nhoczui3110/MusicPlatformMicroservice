package com.MusicPlatForm.user_library_service.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;


//Usee Kafka instead
@FeignClient(name = "file-service",url = "${app.services.file}")
public interface FileClient {
    @PostMapping(value = "/add-playlist-cover",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> addPlaylistCover(@RequestPart MultipartFile cover);

    @PutMapping(value = "/replace-playlist-cover",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> replacePlaylistCover(@RequestPart MultipartFile newCover,@RequestParam String coverName);
}
