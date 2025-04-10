package com.MusicPlatForm.search_service.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.MusicPlatForm.search_service.Dto.Request.AlbumRequest;
import com.MusicPlatForm.search_service.Entity.Album;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    @Mapping(target = "id", ignore = true)
    Album toEntity(AlbumRequest request);

    List<String> toAlbumIds(List<Album> albums);

    default String mapAlbumToId(Album album) {
        return album.getAlbumId();
    }
}
