package com.MusicPlatForm.user_library_service.service;

import org.springframework.stereotype.Service;

import com.MusicPlatForm.user_library_service.repository.PlaylistTrackRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlaylistTrackService {
    private PlaylistTrackRepository playlistTrackRepository;
    public void deleteAllByPlaylistId(String playlistId){
        playlistTrackRepository.deleteAllPlaylistByPlaylistId(playlistId);
    }
}
