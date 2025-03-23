package com.MusicPlatForm.user_library_service.mapper.Playlist;


import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.UpdatePlaylistInfoRequest;
import com.MusicPlatForm.user_library_service.dto.response.playlist.PlaylistResponse;
import com.MusicPlatForm.user_library_service.entity.Playlist;
import com.MusicPlatForm.user_library_service.entity.PlaylistTag;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "likedPlaylists", ignore = true)
    @Mapping(target = "playlistTags", ignore = true)
    @Mapping(target = "playlistTracks", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "genreId", ignore = true)
    @Mapping(target = "releaseDate", ignore = true)
    Playlist toPlaylist(AddPlaylistRequest addPlaylistRequest);

    /**
    *<pre>{@code
    playlist.setDescription(addPlaylistRequest.getDescription());
    playlist.setGenreId(addPlaylistRequest.getGenreId());
    playlist.setId(addPlaylistRequest.getId());
    playlist.setPrivacy(addPlaylistRequest.getPrivacy());
    playlist.setReleaseDate(addPlaylistRequest.getReleaseDate());
    playlist.setTitle(addPlaylistRequest.getTitle());
    * }</pre>
     * @param addPlaylistRequest
     * @param playlist
     */
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "likedPlaylists", ignore = true)
    @Mapping(target = "playlistTags", ignore = true)
    @Mapping(target = "playlistTracks", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updatePlaylistFromRequest(UpdatePlaylistInfoRequest addPlaylistRequest,@MappingTarget Playlist playlist);
    
    
    List<PlaylistResponse> toPlaylistResponses(List<Playlist> playlist);
    

    PlaylistResponse toPlaylistResponse(Playlist playlist);
    
    // for toPlaylistResponse
    default List<String> mapTagsToString(List<PlaylistTag> playlistTags) {
        if (playlistTags == null) {
            return null;
        }
        return playlistTags.stream()
                .map(PlaylistTag::getTagId) // Hoặc getId() nếu muốn lấy ID
                .collect(Collectors.toList());
    }
}
