package com.MusicPlatForm.search_service.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MusicPlatForm.search_service.Dto.ApiResponse;
import com.MusicPlatForm.search_service.Dto.Request.TrackRequest;
import com.MusicPlatForm.search_service.Dto.Response.TrackResponse;
import com.MusicPlatForm.search_service.Entity.Track;
import com.MusicPlatForm.search_service.Service.TrackSearchService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tracks")
@AllArgsConstructor
public class SearchTrackRestController {

    private TrackSearchService trackSearchService;
    private KafkaTemplate<String,TrackRequest> kafkaTemplate;
    @PostMapping("test")
    public void testKafka(){
        TrackRequest track = new TrackRequest();
        track.setName("abc");
        kafkaTemplate.send("add_track_to_search", track);
    }

    @KafkaListener(topics = "add_track_to_search")
    public void addUser(TrackRequest track){
        System.out.println(track);
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Track>> addTrack(@Valid @RequestBody Track track){
        Track savedTrack = trackSearchService.save(track);
        return ResponseEntity.ok().body(
            ApiResponse.<Track>builder().code(200).data(savedTrack).message("Add Track successfully").build()
        );
    }
    @GetMapping("")
    public ResponseEntity<?> searchTracks(@RequestParam String query){
        return ResponseEntity.ok().body(
            ApiResponse.<List<TrackResponse>>builder().code(200).data(this.trackSearchService.searchTracks(query)).message("Successfully").build()
        );
    }
}
