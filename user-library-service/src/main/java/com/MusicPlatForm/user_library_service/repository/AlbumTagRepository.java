package com.MusicPlatForm.user_library_service.repository;

import com.MusicPlatForm.user_library_service.entity.AlbumTag;
import com.MusicPlatForm.user_library_service.entity.AlbumTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumTagRepository extends JpaRepository<AlbumTag, String> {
    List<AlbumTag> findAllByAlbum_Id(String albumId);
}
