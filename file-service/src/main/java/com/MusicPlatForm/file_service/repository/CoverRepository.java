package com.MusicPlatForm.file_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MusicPlatForm.file_service.entity.Cover;

public interface CoverRepository extends JpaRepository<Cover,String>{
    
}
