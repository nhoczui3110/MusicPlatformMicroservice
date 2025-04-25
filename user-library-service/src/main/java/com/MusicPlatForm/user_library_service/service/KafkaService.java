package com.MusicPlatForm.user_library_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.MusicPlatForm.user_library_service.dto.request.kafka.AlbumKafkaRequest;
import com.MusicPlatForm.user_library_service.dto.request.kafka.PlaylistKafkaRequest;
import com.MusicPlatForm.user_library_service.entity.Album;
import com.MusicPlatForm.user_library_service.entity.Playlist;

import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaService {
    KafkaTemplate<String,Object> kafkaTemplate;
    public void sendAlbumToSearchService(Album album){
        AlbumKafkaRequest albumKafkaRequest = new AlbumKafkaRequest();
        albumKafkaRequest.setAlbumId(album.getId());
        albumKafkaRequest.setDescription(album.getDescription());
        albumKafkaRequest.setTitle(album.getAlbumTitle());
        this.kafkaTemplate.send("add_album_to_search",albumKafkaRequest);
    }
    public void sendPlaylistToSearchService(Playlist playlist){
        PlaylistKafkaRequest playlistKafkaRequest = new PlaylistKafkaRequest();
        playlistKafkaRequest.setPlaylistId(playlist.getId());
        playlistKafkaRequest.setDescription(playlist.getDescription());
        playlistKafkaRequest.setTitle(playlist.getTitle());
        this.kafkaTemplate.send("add_playlist_to_search",playlistKafkaRequest);
    }

    public void deletePlaylistFromSearchService(String playlistId){
        this.kafkaTemplate.send("delete_playlist_from_search",playlistId);
    }
    public void deleteAlbumFromSearchService(String albumId){
        this.kafkaTemplate.send("delete_album_from_search",albumId);
    }

    public void sendUpdatedAlbumToSearchService(Album album){
        AlbumKafkaRequest albumKafkaRequest = new AlbumKafkaRequest();
        albumKafkaRequest.setAlbumId(album.getId());
        albumKafkaRequest.setDescription(album.getDescription());
        albumKafkaRequest.setTitle(album.getAlbumTitle());
        this.kafkaTemplate.send("update_album_to_search",albumKafkaRequest);
    }
    public void sendUpdatedPlaylistToSearchService(Playlist playlist){
        PlaylistKafkaRequest playlistKafkaRequest = new PlaylistKafkaRequest();
        playlistKafkaRequest.setPlaylistId(playlist.getId());
        playlistKafkaRequest.setDescription(playlist.getDescription());
        playlistKafkaRequest.setTitle(playlist.getTitle());
        this.kafkaTemplate.send("update_playlist_to_search",playlistKafkaRequest);
    }
}
