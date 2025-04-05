package com.MusicPlatForm.music_service.repository;

import com.MusicPlatForm.music_service.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import com.MusicPlatForm.music_service.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,String> {
    Optional<Genre> findByName(String name);
    List<Genre> findAllByIsUsedTrue();
}
