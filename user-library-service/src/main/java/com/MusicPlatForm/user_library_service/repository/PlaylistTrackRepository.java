package com.MusicPlatForm.user_library_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MusicPlatForm.user_library_service.entity.PlaylistTrack;

@Repository
public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack,String> {
    public void deleteAllPlaylistByPlaylistId(String playlistId);
}
