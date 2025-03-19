package com.MusicPlatForm.user_library_service.mapper.Playlist;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistRequest;
import com.MusicPlatForm.user_library_service.entity.Playlist;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    Playlist toPlaylist(AddPlaylistRequest addPlaylistRequest);
    void updatePlaylistFromRequest(AddPlaylistRequest addPlaylistRequest,@MappingTarget Playlist playlist);
}
