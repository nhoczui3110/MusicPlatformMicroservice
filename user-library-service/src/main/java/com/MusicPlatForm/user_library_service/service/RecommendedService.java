package com.MusicPlatForm.user_library_service.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import com.MusicPlatForm.user_library_service.entity.History;
import com.MusicPlatForm.user_library_service.httpclient.MusicClient;
import com.MusicPlatForm.user_library_service.repository.HistoryRepository;
import com.MusicPlatForm.user_library_service.service.iface.RecommendedServiceInterface;


@Service
  
public class RecommendedService implements RecommendedServiceInterface{
    @Value("${track.related.limit}")
    private int relatedTrackLimit;
    @Value("${history.top-recentlyplayed.limit}")
    private int recentlyPlayedLimit;
    @Value("${history.size-of-list}")
    private int listSize;
    private MusicClient musicClient;
    private HistoryRepository historyRepository;
    public RecommendedService(MusicClient musicClient,HistoryRepository historyRepository){
        this.musicClient = musicClient;
        this.historyRepository = historyRepository;
    }
    @Override
    public List<List<TrackResponse>> getMixedForUser() {
        List<History> histories = this.historyRepository.findTopRecentlyPlayed(recentlyPlayedLimit);
        Collections.shuffle(histories);
        List<History> list = histories.subList(0, Math.min(histories.size(), listSize));
        List<String> topIds = list.stream().map(h->h.getTrackId()).toList();
        List<List<TrackResponse>> listOfListRelatedTracks = musicClient.getRelatedTracksForIds(topIds, relatedTrackLimit).getData();
        for(var trackResponses:listOfListRelatedTracks){
            if(trackResponses.size()<relatedTrackLimit){
                var addMoreTrack = musicClient.getRandomTracks(recentlyPlayedLimit-trackResponses.size()).getData();
                trackResponses.addAll(addMoreTrack);
            }
        }
        return listOfListRelatedTracks;
    }

    

    @Override
    public Map<String, List<TrackResponse>> getGroupedTrackByTags() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupedTrackByTags'");
    }




    @Override
    public List<TrackResponse> getRelatedTrack(String genreId) {
        List<TrackResponse> trackResponses = musicClient.getTracksByGenre(genreId, relatedTrackLimit).getData();
        return trackResponses;
    }
    
}
