package com.MusicPlatForm.music_service.service.iface;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.music_service.dto.reponse.TrackResponse;
import com.MusicPlatForm.music_service.dto.request.TrackRequest;


@Service
public interface TrackServiceInterface {
    /**
     * Upload track
     * @param coverImage
     * @param trackRequest
     */
    public TrackResponse uploadTrack(MultipartFile coverImage,MultipartFile trackAudio,TrackRequest trackRequest);
    
    /**
     * Get track by id
     * @param id
     * @return TrackDto
     * 
     */
    public TrackResponse getTrackById(String id);

    /**
     * Get list of tracks by their id
     * @param ids
     * @return List TrackDto
     */
    public List<TrackResponse> getTracksByIds(List<String> ids);
}
