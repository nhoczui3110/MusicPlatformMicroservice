package com.MusicPlatForm.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MusicPlatForm.music_service.entity.Track;

public interface TrackRepository extends JpaRepository<Track,String>{
    
}
