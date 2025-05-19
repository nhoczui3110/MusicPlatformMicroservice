package com.MusicPlatForm.user_library_service.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.MusicPlatForm.user_library_service.entity.History;
@Repository
public interface HistoryRepository extends JpaRepository<History,String> {
    public List<History> findAllByUserIdOrderByListenedAtDesc(String userId);
    @Query("SELECT h FROM History h WHERE h.userId = :userId AND h.trackId = :trackId ORDER BY h.listenedAt ASC")
    History findFirstByUserIdAndTrackIdOrderByListenedAtAsc(String userId, String trackId);
    public void deleteByUserIdAndTrackId(String userId,String trackId);
    public void deleteAllByUserId(String userId);

    @Query(value = "SELECT TOP(:limit) * FROM History WHERE UserId=:userId ORDER BY Listened_at", nativeQuery = true)
    public List<History> findTopRecentlyPlayed(int limit,String userId);
    
    @Query(value = "FROM History WHERE trackId In :trackIds AND listenedAt BETWEEN :fromDate AND :toDate  ORDER BY listenedAt")
    public List<History> findHistoryOfTracksFromDateToDate(LocalDateTime fromDate, LocalDateTime toDate,List<String> trackIds);
    @Query(value = "FROM History WHERE listenedAt BETWEEN :fromDate AND :toDate  ORDER BY listenedAt")
    public List<History> findAllHistoryFromDateToDate(LocalDateTime fromDate, LocalDateTime toDate);
}
