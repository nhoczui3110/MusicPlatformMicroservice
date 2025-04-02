package com.MusicPlatForm.user_library_service.httpclient;

import com.MusicPlatForm.user_library_service.dto.request.playlist.client.TrackRequest;
import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "music-service",url = "${app.services.music}")
public interface MusicClient {
    @PostMapping(value = "/track/add/multi",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<TrackResponse>> addMultiTrack(@RequestPart MultipartFile[] trackFiles,
                                                          @RequestPart TrackRequest[] trackRequests);
//    @GetMapping("/{id}")
//    public ApiResponse<List<TrackResponse>> getTrackById(@PathVariable String id);
    @GetMapping("/track/{id}")
    public ApiResponse<TrackResponse> getTrackById(@PathVariable String id);
    @GetMapping("/track/list")
    public ApiResponse<List<TrackResponse>> getTrackByIds(@RequestParam List<String> ids);
    @GetMapping("/track/list-by-genre")
    public ApiResponse<List<TrackResponse>> getTracksByGenre(@RequestParam String genreId,@RequestParam  int limit);
    @GetMapping("/track/list-ids-related")
    public ApiResponse<List<List<TrackResponse>>> getRelatedTracksForIds(@RequestParam(name = "track-ids") List<String> trackIds, @RequestParam(name = "limit") int limit);
    @GetMapping("/tracks/random")
    public ApiResponse<List<TrackResponse>> getRandomTracks(@RequestParam int limit);
}
