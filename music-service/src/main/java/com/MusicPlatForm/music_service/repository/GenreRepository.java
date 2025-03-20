package com.MusicPlatForm.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MusicPlatForm.music_service.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre,String> {
    
}
