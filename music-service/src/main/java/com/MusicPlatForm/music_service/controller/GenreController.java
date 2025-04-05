package com.MusicPlatForm.music_service.controller;

import com.MusicPlatForm.music_service.dto.reponse.ApiResponse;
import com.MusicPlatForm.music_service.dto.reponse.GenreResponse;
import com.MusicPlatForm.music_service.dto.reponse.TagResponse;
import com.MusicPlatForm.music_service.dto.request.GenreRequest;
import com.MusicPlatForm.music_service.dto.request.TagRequest;
import com.MusicPlatForm.music_service.service.implement.GenreService;
import com.MusicPlatForm.music_service.service.implement.TagService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreController {
    GenreService genreService;
    @GetMapping()
    public ApiResponse<List<GenreResponse>> getGenres() {
        return ApiResponse.<List<GenreResponse>>builder().data(genreService.getGenres()).build();
    }
}
