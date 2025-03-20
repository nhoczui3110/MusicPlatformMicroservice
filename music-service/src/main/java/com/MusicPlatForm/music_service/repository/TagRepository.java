package com.MusicPlatForm.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MusicPlatForm.music_service.entity.Tag;

public interface TagRepository extends JpaRepository<Tag,String>{
    
}
