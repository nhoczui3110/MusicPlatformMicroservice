package com.MusicPlatForm.music_service.controller;

import com.MusicPlatForm.music_service.dto.reponse.ApiResponse;
import com.MusicPlatForm.music_service.dto.reponse.TagResponse;
import com.MusicPlatForm.music_service.dto.request.TagRequest;
import com.MusicPlatForm.music_service.service.implement.TagService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagController {
    TagService tagService;
    @GetMapping()
    public ApiResponse<List<TagResponse>> getTags() {
        return ApiResponse.<List<TagResponse>>builder().data(tagService.getTags()).build();
    }
}
