package com.MusicPlatForm.user_library_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MusicPlatForm.user_library_service.entity.LikedPlaylist;

@Repository
public interface LikedPlaylistRepository extends JpaRepository<LikedPlaylist,String> {
    
}
