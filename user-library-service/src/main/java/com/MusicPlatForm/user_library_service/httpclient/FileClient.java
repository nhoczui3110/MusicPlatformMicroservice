package com.MusicPlatForm.user_library_service.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;


//Usee Kafka instead
@FeignClient(name = "file-service",url = "${app.services.file}")
public interface FileClient {
    @PostMapping(value = "/image/add-cover",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AddCoverFileResponse> addCover(@RequestPart MultipartFile cover);


    @PutMapping(value = "/image/replace-cover",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AddCoverFileResponse> replaceCover(@RequestPart MultipartFile cover, @RequestPart String oldCoverName);

}
