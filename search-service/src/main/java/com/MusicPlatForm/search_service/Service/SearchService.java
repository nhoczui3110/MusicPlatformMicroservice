package com.MusicPlatForm.search_service.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MusicPlatForm.search_service.Dto.Response.SearchResultResponse;
import com.MusicPlatForm.search_service.Dto.Response.TrackResponse;
import com.MusicPlatForm.search_service.Dto.Response.UserResponse;


@Service
public class SearchService {
    @Autowired
    private TrackSearchService trackSearchService;
    @Autowired
    private UserSearchService userSearchService;

    public List<SearchResultResponse> search(String query){
        List<SearchResultResponse> results = new ArrayList<>();
        List<UserResponse> users = this.userSearchService.searchUsers(query);
        List<TrackResponse> tracks = this.trackSearchService.searchTracks(query);
        results.addAll(users);
        results.addAll(tracks);
        return results;
    }
}
