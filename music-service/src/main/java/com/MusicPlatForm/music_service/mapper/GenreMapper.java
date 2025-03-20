package com.MusicPlatForm.music_service.mapper;

import org.mapstruct.Mapper;

import com.MusicPlatForm.music_service.dto.reponse.GenreResponse;
import com.MusicPlatForm.music_service.entity.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreResponse toGenreResponseFromGenre(Genre genre);
}
