package com.MusicPlatForm.user_library_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MusicPlatForm.user_library_service.dto.request.playlist.PlaylistTrackRequest;
import com.MusicPlatForm.user_library_service.entity.Playlist;
import com.MusicPlatForm.user_library_service.entity.PlaylistTrack;
import com.MusicPlatForm.user_library_service.exception.AppException;
import com.MusicPlatForm.user_library_service.exception.ErrorCode;
import com.MusicPlatForm.user_library_service.mapper.Playlist.PlaylistTrackMapper;
import com.MusicPlatForm.user_library_service.repository.PlaylistRepository;
import com.MusicPlatForm.user_library_service.repository.PlaylistTrackRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PlaylistTrackService {
    PlaylistTrackRepository playlistTrackRepository;
    PlaylistRepository playlistRepository;
    PlaylistTrackMapper playlistTrackMapper;
    public void deleteAllByPlaylistId(String playlistId){
        playlistTrackRepository.deleteAllPlaylistByPlaylistId(playlistId);
    }

    public void deleteTrackFromPlaylist(PlaylistTrackRequest trackRequest){
        PlaylistTrack playlistTrack = this.playlistTrackRepository.findByTrackIdAndPlaylistId(trackRequest.getTrackId(), trackRequest.getPlaylistId());
        
        this.playlistTrackRepository.delete(playlistTrack);
    }
    public void addTrackToPlaylist(PlaylistTrackRequest trackRequest){
        PlaylistTrack playlistTrack = playlistTrackMapper.playlistTrackRequestToPlaylistTrack(trackRequest);
        Playlist playlist = playlistRepository.findById(trackRequest.getPlaylistId())
                                                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
        playlistTrack.setPlaylist(playlist);
        this.playlistTrackRepository.save(playlistTrack);
    }
}
