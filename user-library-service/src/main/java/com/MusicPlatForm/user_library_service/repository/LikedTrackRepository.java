package com.MusicPlatForm.user_library_service.repository;

import com.MusicPlatForm.user_library_service.entity.LikedPlaylist;
import com.MusicPlatForm.user_library_service.entity.LikedTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedTrackRepository extends JpaRepository<LikedTrack,String> {
    public List<LikedTrack> findAllByUserId(String userId);
    LikedTrack findByUserIdAndTrackId(String userId, String trackId);
    int countByTrackId(String trackId);
}
