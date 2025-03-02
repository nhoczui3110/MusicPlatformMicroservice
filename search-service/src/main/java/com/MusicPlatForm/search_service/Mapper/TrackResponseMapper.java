package com.MusicPlatForm.search_service.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.MusicPlatForm.search_service.Dto.Response.TrackResponse;
import com.MusicPlatForm.search_service.Entity.Track;

@Mapper(componentModel = "spring")
public interface TrackResponseMapper {
    @Mapping(target = "type", constant = "track") 
    TrackResponse toTrackResponse(Track track);
    @Mapping(target = "type", constant = "track") 
    List<TrackResponse> toTrackResponseList(List<Track> tracks);

}
