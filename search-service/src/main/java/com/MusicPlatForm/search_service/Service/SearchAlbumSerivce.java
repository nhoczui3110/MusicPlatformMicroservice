package com.MusicPlatForm.search_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MusicPlatForm.search_service.Dto.Request.AlbumRequest;
import com.MusicPlatForm.search_service.Entity.Album;
import com.MusicPlatForm.search_service.Mapper.AlbumMapper;
import com.MusicPlatForm.search_service.Repository.AlbumRepository;

@Service
public class SearchAlbumSerivce {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    public Album save(AlbumRequest request) {
        Album album = albumMapper.toEntity(request);
        return albumRepository.save(album);
    }
    public void deleteAlbumByAlbumId(String albumId) {
        albumRepository.deleteByAlbumId(albumId);
    }
    public List<String> searchAlbums(String query) {
        List<Album> albums = albumRepository.findAlbums(query);
        return albumMapper.toAlbumIds(albums);
    }
}
