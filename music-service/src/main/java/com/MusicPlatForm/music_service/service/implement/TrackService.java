package com.MusicPlatForm.music_service.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.MusicPlatForm.music_service.dto.reponse.*;
import com.MusicPlatForm.music_service.dto.kafka_request.*;
import com.MusicPlatForm.music_service.dto.request.UpdateTrackRequest;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.music_service.dto.request.TrackRequest;
import com.MusicPlatForm.music_service.entity.Genre;
import com.MusicPlatForm.music_service.entity.Tag;
import com.MusicPlatForm.music_service.entity.Track;
import com.MusicPlatForm.music_service.entity.TrackTag;
import com.MusicPlatForm.music_service.exception.AppException;
import com.MusicPlatForm.music_service.exception.ErrorCode;
import com.MusicPlatForm.music_service.httpclient.FileClient;
import com.MusicPlatForm.music_service.mapper.GenreMapper;
import com.MusicPlatForm.music_service.mapper.TagMapper;
import com.MusicPlatForm.music_service.mapper.TrackMapper;
import com.MusicPlatForm.music_service.repository.*;
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
    // private KafkaTemplate<String,com.MusicPlatForm.music_service.dto.kafka_request.TrackRequest> kafkaTemplate;
    // private void sendTrackToSearchService(Track track){
    //     com.MusicPlatForm.music_service.dto.kafka_request.TrackRequest request = new com.MusicPlatForm.music_service.dto.kafka_request.TrackRequest();
    //     request.setTrackId(track.getId());
    //     request.setDescription(track.getDescription());
    //     request.setName(track.getTitle());
    //     kafkaTemplate.send("add_track_to_search", request);

    // }
    @Override
    @Transactional
    public TrackResponse uploadTrack(MultipartFile coverImage, MultipartFile trackAudio,TrackRequest trackRequest) {
        ApiResponse<AddCoverFileResponse> uploadTrackCoverResponse = null;
        if (coverImage != null && !coverImage.isEmpty()) {
            uploadTrackCoverResponse = fileClient.addCover(coverImage);
        }
        ApiResponse<AddTrackFileResponse> uploadTrackMp3Response = fileClient.addTrack(trackAudio);
        Track track = trackMapper.toTrackFromTrackRequest(trackRequest);
        
        if (uploadTrackCoverResponse != null) {
                track.setCoverImageName(uploadTrackCoverResponse.getData().getCoverName());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        track.setUserId(userId);
        track.setCreatedAt(LocalDateTime.now());
        track.setCountPlay(0);
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
        // this.sendTrackToSearchService(savedTrack);
        TrackResponse trackResponse = this.trackMapper.toTrackResponseFromTrack(savedTrack);
        trackResponse.setTags(this.tagMapper.toTagResponsesFromTags(tags));
        trackResponse.setGenre(this.genreMapper.toGenreResponseFromGenre(genre));

        return trackResponse;
    }

    public List<TrackResponse> uploadTracks(List<MultipartFile> trackFiles, List<TrackRequest> trackRequests){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        List<TrackResponse> trackResponses = new ArrayList<>();
        ApiResponse<List<AddTrackFileResponse>> addTrackFilesResponse = fileClient.addTracks(trackFiles);
        int index = 0;
        for(TrackRequest trackRequest: trackRequests){
            Track track = trackMapper.toTrackFromTrackRequest(trackRequest);
            track.setCreatedAt(LocalDateTime.now());
            track.setCountPlay(0);
            track.setDuration(addTrackFilesResponse.getData().get(index).getDuration());
            track.setFileName(addTrackFilesResponse.getData().get(index).getTrack());
            track.setUserId(userId);
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
            trackResponses.add(trackResponse);
        }
        return trackResponses;
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

        List<Track> tracks = this.trackRepository.findAllById(ids);
        List<TrackResponse> trackResponses = new ArrayList<>();
        for(Track track:tracks){
            TrackResponse trackResponse = trackMapper.toTrackResponseFromTrack(track);
            List<Tag> tags = track.getTrackTags().stream().map(trackTag->trackTag.getTag()).toList();
            trackResponse.setTags(tagMapper.toTagResponsesFromTags(tags));
            trackResponse.setGenre(genreMapper.toGenreResponseFromGenre(track.getGenre()));
            trackResponses.add(trackResponse);
        }
        return trackResponses;
    }

    @Override
    public void deleteTrack(String trackId){
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String userId = authentication.getName();
        String userId = "user1";
        Track track = trackRepository.findById(trackId).orElseThrow(()->new AppException(ErrorCode.TRACK_NOT_FOUND));
        if(!track.getUserId().equals(userId)) throw new AppException(ErrorCode.UNAUTHORIZED);
        trackRepository.delete(track);

        
    }

    @Override
    public List<TrackResponse> getTracksByGenre(String genreId,int limit) {
        List<Track> tracks= trackRepository.findRandomTracksByGenre(genreId,limit);
        List<TrackResponse> trackResponses = new ArrayList<>();
        for(Track track:tracks){
            TrackResponse trackResponse = trackMapper.toTrackResponseFromTrack(track);
            List<Tag> tags = track.getTrackTags().stream().map(trackTag->trackTag.getTag()).toList();
            trackResponse.setTags(tagMapper.toTagResponsesFromTags(tags));
            // trackResponse.setGenre(genreMapper.toGenreResponseFromGenre(track.getGenre()));
            trackResponses.add(trackResponse);
        }
        return trackResponses;
    }

    @Override
    public List<List<TrackResponse>> getRelatedTracksForIds(List<String> ids,int limit) {
        List<Track> tracks = this.trackRepository.findAllById(ids);
        List<List<TrackResponse>> listOfListTrackResponses = new ArrayList<>();
        List<TrackResponse> trackResponses;
        TrackResponse trackResponse;
        for(Track track:tracks){
            Genre genre = track.getGenre();
            if(genre !=null){
                trackResponses= getTracksByGenre(genre.getId(), limit-1);
                trackResponses.addFirst(trackMapper.toTrackResponseFromTrack(track));
            }
            else{
                trackResponse = trackMapper.toTrackResponseFromTrack(track);
                trackResponses = List.of(trackResponse);
            }
            listOfListTrackResponses.add(trackResponses);
        }
        return listOfListTrackResponses;
    }

    @Override
    public List<TrackResponse> getRandomTracks(int limit) {
        List<Track> tracks = this.trackRepository.findRandomTracks(limit);
        List<TrackResponse> trackResponses = new ArrayList<>();
        for(Track track:tracks){
            TrackResponse trackResponse = trackMapper.toTrackResponseFromTrack(track);
            List<Tag> tags = track.getTrackTags().stream().map(trackTag->trackTag.getTag()).toList();
            trackResponse.setTags(tagMapper.toTagResponsesFromTags(tags));
            trackResponse.setGenre(genreMapper.toGenreResponseFromGenre(track.getGenre()));
            trackResponses.add(trackResponse);
        }
        return trackResponses;
    }

    @Override
    public TrackResponse updateTrack(String trackId, UpdateTrackRequest request, MultipartFile imageFile, MultipartFile trackFile) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new AppException(ErrorCode.TRACK_NOT_FOUND));

        String createdUserId = track.getUserId();

        List<TagResponse> tagResponses = new ArrayList<>();
        GenreResponse genreResponse = null;

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags =  tagRepository.findAllById(request.getTagIds());
            List<TrackTag> trackTags = tags.stream()
                    .map(tag -> new TrackTag(tag, track))
                    .toList();
            if (!trackTags.isEmpty()) {
                track.getTrackTags().clear();
                track.getTrackTags().addAll(trackTags);
                tagResponses = tagMapper.toTagResponsesFromTags(tags);
            }
        }

        if (request.getGenreId() != null && !request.getGenreId().isEmpty()) {
            Genre genre = genreRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND));
            track.setGenre(genre);
            genreResponse = genreMapper.toGenreResponseFromGenre(genre);
        }

        trackMapper.updateTrack(track, request);

        if (imageFile != null) {
            ApiResponse<AddCoverFileResponse> response = fileClient.addCover(imageFile);
            track.setCoverImageName(response.getData().getCoverName());
        }

        if (trackFile != null) {
            ApiResponse<AddTrackFileResponse> fileResponse = fileClient.addTrack(trackFile);
            track.setFileName(fileResponse.getData().getTrack());
        }

        TrackResponse response = trackMapper.toTrackResponseFromTrack(trackRepository.save(track));

        if (!tagResponses.isEmpty()) {
            response.setTags(tagResponses);
        }
        if (genreResponse != null) {
            response.setGenre(genreResponse);
        }

        return response;
    }

    @Override
    public List<TrackResponse> getTracksByUserId(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userId==null || userId==""){
            userId = authentication.getName();
        }
        List<Track> tracks = this.trackRepository.findTrackByUserId(userId);
        List<TrackResponse> trackResponses = new ArrayList<>();
        for(Track track:tracks){
            TrackResponse trackResponse = trackMapper.toTrackResponseFromTrack(track);
            List<Tag> tags = track.getTrackTags().stream().map(trackTag->trackTag.getTag()).toList();
            trackResponse.setTags(tagMapper.toTagResponsesFromTags(tags));
            trackResponse.setGenre(genreMapper.toGenreResponseFromGenre(track.getGenre()));
            trackResponses.add(trackResponse);
        }
        return trackResponses;
    }
}
