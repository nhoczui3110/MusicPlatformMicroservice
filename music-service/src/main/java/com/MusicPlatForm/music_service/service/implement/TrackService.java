package com.MusicPlatForm.music_service.service.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.music_service.dto.reponse.AddCoverFileResponse;
import com.MusicPlatForm.music_service.dto.reponse.AddTrackFileResponse;
import com.MusicPlatForm.music_service.dto.reponse.ApiResponse;
import com.MusicPlatForm.music_service.dto.reponse.TrackResponse;
import com.MusicPlatForm.music_service.dto.request.TrackRequest;
import com.MusicPlatForm.music_service.entity.Genre;
import com.MusicPlatForm.music_service.entity.Tag;
import com.MusicPlatForm.music_service.entity.Track;
import com.MusicPlatForm.music_service.entity.TrackTag;
import com.MusicPlatForm.music_service.httpclient.FileClient;
import com.MusicPlatForm.music_service.mapper.GenreMapper;
import com.MusicPlatForm.music_service.mapper.TagMapper;
import com.MusicPlatForm.music_service.mapper.TrackMapper;
import com.MusicPlatForm.music_service.repository.GenreRepository;
import com.MusicPlatForm.music_service.repository.TagRepository;
import com.MusicPlatForm.music_service.repository.TrackRepository;
import com.MusicPlatForm.music_service.service.iface.TrackServiceInterface;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TrackService implements TrackServiceInterface{
    FileClient fileClient;
    TrackMapper trackMapper;
    TagMapper tagMapper;
    GenreMapper genreMapper;
    TagRepository tagRepository;
    TrackRepository trackRepository;
    GenreRepository genreRepository;
    @Override
    @Transactional
    public TrackResponse uploadTrack(MultipartFile coverImage, MultipartFile trackAudio,TrackRequest trackRequest) {
        ApiResponse<AddCoverFileResponse> uploadTrackCoverResponse = fileClient.addCover(coverImage);
        ApiResponse<AddTrackFileResponse> uploadTrackMp3Response = fileClient.addTrack(trackAudio);
        Track track = trackMapper.toTrackFromTrackRequest(trackRequest);
        
        track.setCoverImageName(uploadTrackCoverResponse.getData().getCoverName());
        track.setCreatedAt(LocalDateTime.now());
        track.setDuration(uploadTrackMp3Response.getData().getDuration());
        track.setFileName(uploadTrackMp3Response.getData().getTrack());

        List<Tag> tags = this.tagRepository.findAllById(trackRequest.getTagIds());
        List<TrackTag> trackTags = tags.stream()
                                    .map(tag-> new TrackTag(tag,track))
                                    .collect(Collectors.toList());
        track.setTrackTags(trackTags);
        
        Genre genre = this.genreRepository.findById(trackRequest.getGenreId()).orElse(null);
        track.setGenre(genre);
        Track savedTrack =  trackRepository.save(track);

        TrackResponse trackResponse = this.trackMapper.toTrackResponseFromTrack(savedTrack);
        trackResponse.setTags(this.tagMapper.toTagResponsesFromTags(tags));
        trackResponse.setGenre(this.genreMapper.toGenreResponseFromGenre(genre));
        return trackResponse;
    }

    @Override
    public TrackResponse getTrackById(String id) {

        Track track = this.trackRepository.findById(id).orElseThrow();
        TrackResponse trackResponse = trackMapper.toTrackResponseFromTrack(track);
        List<Tag> tags = track.getTrackTags().stream().map(trackTag->trackTag.getTag()).toList();
        trackResponse.setTags(tagMapper.toTagResponsesFromTags(tags));
        trackResponse.setGenre(genreMapper.toGenreResponseFromGenre(track.getGenre()));
        return trackResponse;
    }

    @Override
    public List<TrackResponse> getTracksByIds(List<String> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTracksByIds'");
    }
    
}
