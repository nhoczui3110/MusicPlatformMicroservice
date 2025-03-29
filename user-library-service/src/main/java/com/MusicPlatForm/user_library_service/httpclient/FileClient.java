package com.MusicPlatForm.user_library_service.httpclient;

import com.MusicPlatForm.user_library_service.dto.response.CoverRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;


//Usee Kafka instead
@FeignClient(name = "file-service",url = "${app.services.file}")
public interface FileClient {
    @PostMapping(value = "/image/add-cover",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AddCoverFileResponse> addCover(@RequestPart MultipartFile cover);


    @PutMapping(value = "/image/replace-cover",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AddCoverFileResponse> replaceCover(@RequestPart MultipartFile newCover, @RequestPart String oldCoverName);

    @DeleteMapping(value = "/image/delete-cover", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<String> deleteAvatar(@RequestBody CoverRequest request);

    @DeleteMapping(value = "/image/delete-cover/{cover_name}")
    public ApiResponse<String> deleteCoverImage(@PathVariable String coverName);
}
