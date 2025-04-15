package com.MusicPlatForm.search_service.Controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MusicPlatForm.search_service.Dto.ApiResponse;
import com.MusicPlatForm.search_service.Dto.Request.TrackRequest;
import com.MusicPlatForm.search_service.Service.TrackSearchService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tracks")
@AllArgsConstructor
public class SearchTrackRestController {

    private TrackSearchService trackSearchService;
    private KafkaTemplate<String,TrackRequest> kafkaTemplate;

    // ======================Test========================
    @PostMapping("test")
    public void testKafka(){
        TrackRequest track = new TrackRequest();
        track.setName("abc");
        kafkaTemplate.send("add_track_to_search", track);
    }

    // @KafkaListener(topics = "add_track_to_search")
    // public void addUser(TrackRequest track){
    //     System.out.println(track);
    // }



    // Kafka listener to process track data when received from Kafka topic
    @KafkaListener(topics = "add_track_to_search", groupId = "search_group")
    public void addTrackToSearch(TrackRequest trackRequest) {
        // Send the trackRequest to the service to handle track saving
        trackSearchService.save(trackRequest);
    }

    @KafkaListener(topics = "delete_track_from_search", groupId = "search_group")
    public void deleteTrackFromSearch(String trackId) {
        trackSearchService.deleteTrackByTrackId(trackId);
    }


    @GetMapping("")
    public ResponseEntity<?> searchTracks(@RequestParam(name = "q") String query){
        if(query.equals("test")){
            TrackRequest trackRequest = new TrackRequest();
            trackRequest.setName("nhạc hay");
            trackRequest.setDescription("nhạc hay");
            trackRequest.setTrackId("8efd488f-1265-4ae6-9ba7-2cf2bb35d9cfuser456");
            trackSearchService.save(trackRequest);
        }
        return ResponseEntity.ok().body(
            ApiResponse.<List<String>>builder().code(200).data(this.trackSearchService.searchTracks(query)).message("Successfully").build()
        );
    }
}
