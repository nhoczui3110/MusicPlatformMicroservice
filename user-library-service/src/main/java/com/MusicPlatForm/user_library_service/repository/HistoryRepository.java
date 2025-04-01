package com.MusicPlatForm.user_library_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.MusicPlatForm.user_library_service.entity.History;
@Repository
public interface HistoryRepository extends JpaRepository<History,String> {
    public List<History> findAllByUserIdOrderByListenedAtDesc(String userId);
    public History findByUserIdAndTrackId(String userId,String trackId);

    public void deleteAllByUserId(String userId);

    @Query(value = "SELECT TOP(:limit) * FROM History ORDER BY Listened_at", nativeQuery = true)
    public List<History> findTopRecentlyPlayed(int limit);
}
