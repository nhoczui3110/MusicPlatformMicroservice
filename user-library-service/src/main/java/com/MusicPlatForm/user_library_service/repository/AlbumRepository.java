package com.MusicPlatForm.user_library_service.repository;

import com.MusicPlatForm.user_library_service.dto.response.album.AlbumResponse;
import com.MusicPlatForm.user_library_service.entity.Album;
import com.MusicPlatForm.user_library_service.entity.AlbumTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, String> {
    Page<Album> findByUserId(Pageable pageable, String userid);
    Page<Album> findByUserIdAndPrivacy(Pageable pageable, String userid, String privacy);
}
