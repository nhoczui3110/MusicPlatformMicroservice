package com.MusicPlatForm.user_library_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import com.MusicPlatForm.user_library_service.dto.response.history.HistoryResponse;
import com.MusicPlatForm.user_library_service.entity.History;
import com.MusicPlatForm.user_library_service.httpclient.MusicClient;
import com.MusicPlatForm.user_library_service.mapper.Playlist.HistoryMapper;
import com.MusicPlatForm.user_library_service.repository.HistoryRepository;
import com.MusicPlatForm.user_library_service.repository.LikedTrackRepository;
import com.MusicPlatForm.user_library_service.service.iface.HistorySerivceInterface;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class HistoryService implements HistorySerivceInterface {
    HistoryRepository historyRepository;
    HistoryMapper historyMapper;
    LikedTrackRepository likedTrackRepository;
    MusicClient musicClient;
    @Override
    public List<TrackResponse> getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<History> histories = this.historyRepository.findAllByUserIdOrderByListenedAtDesc(userId);
        List<String> trackIds = histories.stream().map(h->h.getTrackId()).toList();
        List<TrackResponse> trackResponses = musicClient.getTrackByIds(trackIds).getData();
        List<String> likedTrackIds =  likedTrackRepository.findAllByUserId(userId).stream().map(l->l.getTrackId()).toList();
        for(var track: trackResponses){
            if(likedTrackIds.contains(track.getId())){
                track.setIsLiked(true);
            }
        }
        return trackResponses;
    }
    @Override
    public HistoryResponse addHistory(String trackId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        History history = this.historyRepository.findByUserIdAndTrackId(userId,trackId);
        if(history==null)
        {
            history = new History();
            history.setUserId(userId);
            history.setTrackId(trackId);
        }
        history.setListenedAt(LocalDateTime.now());
        History savedHistory = this.historyRepository.save(history);
        return historyMapper.toHistoryResponse(savedHistory);
    }
    @Override
    public void clearAllHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        this.historyRepository.deleteAllByUserId(userId);
    }
    @Override
    public void deleteHistoryById(String trackId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        this.historyRepository.deleteByUserIdAndTrackId(userId, trackId);
    }
    
}
