package com.MusicPlatForm.user_library_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
     * Get list of related track by genre
     * @return
     */
    @GetMapping("/related-track")
    public ResponseEntity<ApiResponse<?>> getSimilartToLikedTrack(@RequestParam(name = "genre_id") String genreId){
        return ResponseEntity.ok().body(
            ApiResponse.<List<TrackResponse>>builder()
                                            .code(HttpStatus.OK.value())
                                            .data(recommendedService.getRelatedTrack(genreId))
                                            .build()
        );
    } 
}
