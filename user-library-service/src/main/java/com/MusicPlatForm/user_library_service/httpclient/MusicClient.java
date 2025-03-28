package com.MusicPlatForm.user_library_service.httpclient;

import com.MusicPlatForm.user_library_service.dto.request.playlist.client.TrackRequest;
import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "music-service",url = "${app.services.music}")
public interface MusicClient {
    @PostMapping(value = "/track/add/multi",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<TrackResponse>> addMultiTrack(@RequestPart MultipartFile[] trackFiles,
                                                          @RequestPart TrackRequest[] trackRequests);
}
