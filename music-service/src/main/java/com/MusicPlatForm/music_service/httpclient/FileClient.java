package com.MusicPlatForm.music_service.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.music_service.dto.reponse.AddCoverFileResponse;
import com.MusicPlatForm.music_service.dto.reponse.AddTrackFileResponse;
import com.MusicPlatForm.music_service.dto.reponse.ApiResponse;




//Usee Kafka instead
@FeignClient(name = "file-service",url = "${app.services.file}")
public interface FileClient {

    /**
     * Add track cover
     * @param cover
     * @return ApiResponse<String>: File name
     */
    @PostMapping(value = "/image/add-cover",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AddCoverFileResponse> addCover(@RequestPart MultipartFile cover);
    @PostMapping(value = "/music/add-track",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AddTrackFileResponse> addTrack(@RequestPart MultipartFile track);

}
