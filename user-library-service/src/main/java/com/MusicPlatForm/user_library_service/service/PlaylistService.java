package com.MusicPlatForm.user_library_service.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.UpdatePlaylistInfoRequest;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.entity.Playlist;
import com.MusicPlatForm.user_library_service.entity.PlaylistTrack;
import com.MusicPlatForm.user_library_service.exception.AppException;
import com.MusicPlatForm.user_library_service.exception.ErrorCode;
import com.MusicPlatForm.user_library_service.mapper.Playlist.PlaylistMapper;
import com.MusicPlatForm.user_library_service.mapper.Playlist.PlaylistTrackMapper;
import com.MusicPlatForm.user_library_service.repository.PlaylistRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private PlaylistMapper playlistMapper;
    private PlaylistTrackMapper playlistTrackMapper;
    private PlaylistTrackService playlistTrackService;
    
    public ApiResponse<List<Playlist>> getPlaylists(){
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        

        List<Playlist> playlists = playlistRepository.getPlaylists(userId);

        return ApiResponse.<List<Playlist>>builder()
                                            .data(playlists)
                                            .code(200)
                                            .message("Playlist for user")
                                            .build();
    }

    @Transactional
    public void addPlaylist(AddPlaylistRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        if(userId==null){
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        Playlist playlist = playlistMapper.toPlaylist(request);
        
        List<PlaylistTrack>playlistTracks = playlistTrackMapper.toPlaylistTracks(request.getTracks());
        playlistTracks.forEach(track->{
            track.setPlaylist(playlist);
        });
        playlist.setPlaylistTracks(playlistTracks);
        playlist.setUserId(userId);
        playlistRepository.save(playlist);
        return;
    }

    @Transactional
    public void updatePlaylistInfo(UpdatePlaylistInfoRequest request){

        Playlist playlist = playlistRepository.findById(request.getId())
                                                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
        playlistMapper.updatePlaylistFromRequest(request, playlist);
        
        List<PlaylistTrack>updatedTracks = playlistTrackMapper.toPlaylistTracks(request.getTracks());
        updatedTracks.forEach(track->{
            track.setPlaylist(playlist);
        });
        playlist.getPlaylistTracks().removeIf(track ->
                updatedTracks.stream().noneMatch(updated -> updated.getTrackId() != null && updated.getTrackId().equals(track.getTrackId()))
        );
        
        for (PlaylistTrack updatedTrack : updatedTracks) {
            boolean exists = playlist.getPlaylistTracks().stream()
                    .anyMatch(track -> track.getTrackId() != null && track.getTrackId().equals(updatedTrack.getTrackId()));
            if (!exists) {
                playlist.getPlaylistTracks().add(updatedTrack);
            }
        }
        playlistRepository.save(playlist);
    }
}
