package com.devteria.profile.repository;

import com.devteria.profile.dto.request.ApiResponse;
import com.devteria.profile.dto.request.DeleteAvatarRequest;
import com.devteria.profile.dto.response.UploadAvatarResponse;
import com.devteria.profile.dto.response.UploadCoverResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-client", url = "${app.services.file}")
public interface FileClient {
    @PostMapping(value = "/image/add-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UploadAvatarResponse> addAvatar(@RequestPart("avatar") MultipartFile avatar);
    @DeleteMapping(value = "/image/delete-avatar", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<String> deleteAvatar(@RequestBody DeleteAvatarRequest request);
    @PutMapping(value = "/image/replace-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UploadAvatarResponse> replaceAvatar(@RequestPart("newAvatar") MultipartFile newAvatar,
                                                    @RequestPart("oldAvatarName") String oldAvatarName);
    @PostMapping(value = "/image/add-cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UploadCoverResponse> addCover(@RequestPart("cover") MultipartFile cover);

    @PutMapping(value = "/image/replace-cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UploadCoverResponse> replaceCover(@RequestPart("newCover") MultipartFile newCover,
                                                    @RequestPart("oldCoverName") String oldCoverName);

//    @GetMapping(value = "/avatar/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName);
}
