package com.MusicPlatForm.search_service.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MusicPlatForm.search_service.Dto.ApiResponse;
import com.MusicPlatForm.search_service.Dto.Response.SearchResultResponse;
import com.MusicPlatForm.search_service.Service.SearchService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class SearchRestController {
    private SearchService searchService;
    @GetMapping("")
    public ResponseEntity<?> search(@RequestParam String query){
        return ResponseEntity.ok().body(
            ApiResponse.<List<SearchResultResponse>>builder().code(200).data(searchService.search(query)).build()
        );
    }
}
