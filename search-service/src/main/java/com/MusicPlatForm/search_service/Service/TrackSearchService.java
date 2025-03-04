package com.MusicPlatForm.search_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.MusicPlatForm.search_service.Dto.Response.TrackResponse;
import com.MusicPlatForm.search_service.Entity.Track;
import com.MusicPlatForm.search_service.Mapper.TrackResponseMapper;
import com.MusicPlatForm.search_service.Repository.TrackRepository;


// https://viblo.asia/p/spring-data-elasticsearch-tan-dung-elasticsearch-trong-ung-dung-spring-boot-oK9VyxeALQR
@Service
public class TrackSearchService {
    @Autowired
    private TrackRepository trackRepository;
    @Autowired 
    private TrackResponseMapper trackResponseMapper;
    public Track save(Track track) {
        return trackRepository.save(track);
    }

    public List<TrackResponse> searchByName(String name) {
        List<Track> tracks=  trackRepository.findByName(name);
        return trackResponseMapper.toTrackResponseList(tracks);
    }
    
    public List<TrackResponse> searchByDescription(String description){
        List<Track> tracks= trackRepository.findByDescription(description);
        return trackResponseMapper.toTrackResponseList(tracks);
    }

    public List<TrackResponse> searchTracks(String query){
        List<Track> tracks = trackRepository.findTracks(query);
        return trackResponseMapper.toTrackResponseList(tracks);
    }
}
