package com.MusicPlatForm.user_library_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import com.MusicPlatForm.user_library_service.service.iface.RecommendedServiceInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/recommended")
@RequiredArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RecommendedController {
    RecommendedServiceInterface recommendedService;
    @GetMapping("/mixed-for")
    public ResponseEntity<ApiResponse<?>> getMixforUser(){
        return null;
    } 


    /**
     * Lấy track theo trackId từ đó lấy thể loại và tìm track theo thể loại.
     * Nếu track không có thể loại thì lấy toàn bộ nhạc user đã like làm response;
     * @param trackId
     * @return
     */
    @GetMapping("/related-track/{track_id}")
    public ResponseEntity<ApiResponse<List<TrackResponse>>> getSimilartToLikedTrack(@PathVariable(name = "track_id") String trackId){
        return ResponseEntity.ok().body(
            ApiResponse.<List<TrackResponse>>builder()
                                            .code(HttpStatus.OK.value())
                                            .data(recommendedService.getRelatedTrack(trackId))
                                            .build()
        );
    } 
}
