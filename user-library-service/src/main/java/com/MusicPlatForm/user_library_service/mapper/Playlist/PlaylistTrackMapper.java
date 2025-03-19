package com.MusicPlatForm.user_library_service.mapper.Playlist;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistTrackRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.PlaylistTrackRequest;
import com.MusicPlatForm.user_library_service.dto.response.playlist.PlaylistTrackResponse;
import com.MusicPlatForm.user_library_service.entity.PlaylistTrack;

@Mapper(componentModel = "spring")
public interface PlaylistTrackMapper {
    PlaylistTrack toPlaylistTrack(AddPlaylistTrackRequest addPlaylistTrackRequest);
    List<PlaylistTrack> toPlaylistTracks(List<AddPlaylistTrackRequest> addPlaylistTrackRequests);
    List<PlaylistTrackResponse> toPlaylistTracksResponse(List<PlaylistTrack> playlistTracks);

    /**
     * Only map trackId
     * Add time for addedAt
     * @param playlistTrackRequest
     * @return PlaylistTrack
     */
    @Mapping(target = "addedAt", expression = "java(java.time.LocalDateTime.now())") 
    PlaylistTrack playlistTrackRequestToPlaylistTrack(PlaylistTrackRequest playlistTrackRequest);
    
}
