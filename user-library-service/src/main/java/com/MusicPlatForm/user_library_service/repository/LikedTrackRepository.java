package com.MusicPlatForm.user_library_service.repository;

import com.MusicPlatForm.user_library_service.entity.LikedTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedTrackRepository extends JpaRepository<LikedTrack,String> {
    public List<LikedTrack> findAllByUserId(String userId);
    public LikedTrack findByUserIdAndTrackId(String userId, String trackId);
    public int countByTrackId(String trackId);
    public List<LikedTrack> findByTrackId(String trackId);
}
