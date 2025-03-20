package com.MusicPlatForm.music_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.MusicPlatForm.music_service.dto.reponse.TagResponse;
import com.MusicPlatForm.music_service.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagResponse toTagResponseFromTag(Tag tag);
    List<TagResponse> toTagResponsesFromTags(List<Tag> tags);
}
