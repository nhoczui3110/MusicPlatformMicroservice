package com.MusicPlatForm.user_library_service.mapper.Playlist;

import java.util.List;

import org.mapstruct.Mapper;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistTrackRequest;
import com.MusicPlatForm.user_library_service.entity.PlaylistTrack;

@Mapper(componentModel = "spring")
public interface PlaylistTrackMapper {
    PlaylistTrack toPlaylistTrack(AddPlaylistTrackRequest addPlaylistTrackRequest);
    List<PlaylistTrack> toPlaylistTracks(List<AddPlaylistTrackRequest> addPlaylistTrackRequests);
}
