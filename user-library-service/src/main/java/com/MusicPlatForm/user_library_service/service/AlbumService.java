package com.MusicPlatForm.user_library_service.service;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddTrackAlbumRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.AlbumRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.client.TrackRequest;
import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.album.AlbumResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.GenreResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.TagResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import com.MusicPlatForm.user_library_service.entity.Album;
import com.MusicPlatForm.user_library_service.entity.AlbumTag;
import com.MusicPlatForm.user_library_service.entity.AlbumTrack;
import com.MusicPlatForm.user_library_service.entity.LikedAlbum;
import com.MusicPlatForm.user_library_service.exception.AppException;
import com.MusicPlatForm.user_library_service.exception.ErrorCode;
import com.MusicPlatForm.user_library_service.httpclient.FileClient;
import com.MusicPlatForm.user_library_service.httpclient.MusicClient;
import com.MusicPlatForm.user_library_service.mapper.Playlist.AlbumMapper;
import com.MusicPlatForm.user_library_service.repository.AlbumRepository;
import com.MusicPlatForm.user_library_service.repository.AlbumTrackRepository;
import com.MusicPlatForm.user_library_service.repository.LikedAlbumRepository;
import com.MusicPlatForm.user_library_service.repository.LikedTrackRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumService {
    AlbumRepository albumRepository;
    AlbumTrackRepository albumTrackRepository;
    LikedAlbumRepository likedAlbumRepository;
    LikedTrackRepository likedTrackRepository;
    AlbumMapper albumMapper;
    FileClient fileClient;
    MusicClient musicClient;
    KafkaService kafkaService;

    @Value("${app.services.file}")
    @NonFinal
    String fileServiceUrl;


    private AlbumResponse getFullAlbumResponse(Album album){
        List<String> trackIds = new ArrayList<>();
        List<String> tagIds = new ArrayList<>();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggingUserId = (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken)
                ? null : authentication.getName();
        List<String> likedTrackIds = (loggingUserId == null) ? null
                : likedTrackRepository.findAllByUserId(loggingUserId).stream()
                        .map(track -> track.getTrackId()).toList();

        for(var tag: album.getTags()){
            tagIds.add(tag.getTagId());
        }
        for(var track: album.getTracks()){
            trackIds.add(track.getTrackId());
        }
        ApiResponse<List<TrackResponse>> tracksResponse =  musicClient.getTrackByIds(trackIds);
        ApiResponse<List<TagResponse>> tagsResponse = musicClient.getTagsByIds(tagIds);
        AlbumResponse response = albumMapper.toAlbumResponse(album);
        if(album.getGenreId()!=null){
            ApiResponse<GenreResponse> genre = musicClient.getGenreById(album.getGenreId());
            response.setGenre(genre.getData());
        }
        if(likedTrackIds!=null){
            tracksResponse.getData().forEach((track)->{
                if(likedTrackIds.contains(track.getId()))
                track.setIsLiked(true);
            });
        }
        if(loggingUserId!=null){
            response.setIsLiked(likedAlbumRepository.existsByUserIdAndAlbumId(loggingUserId, album.getId()));
        }
        response.setTracks(tracksResponse.getData());
        response.setTags(tagsResponse.getData());
        return response;
    }


    @Transactional
    public AlbumResponse addAlbum(AlbumRequest request, MultipartFile coverAlbum, List<MultipartFile> trackFiles, List<TrackRequest> trackRequests) throws JsonProcessingException {
        if (albumRepository.findByAlbumLink(request.getAlbumLink()).isPresent()) {
            throw new AppException(ErrorCode.ALBUM_LINK_EXISTED);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Album newAlbum = albumMapper.toAlbum(request);
        ApiResponse<List<TrackResponse>> trackResponseList = null;
        newAlbum.setUserId(userId);
        if (coverAlbum != null && !coverAlbum.isEmpty()) {
            ApiResponse<AddCoverFileResponse> coverFileResponse = fileClient.addCover(coverAlbum);
            String nameImage = coverFileResponse.getData().getCoverName();
            newAlbum.setImagePath(nameImage);
        }
        if (trackFiles != null && !trackFiles.isEmpty() && trackRequests != null && !trackRequests.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String trackRequestJson = objectMapper.writeValueAsString(trackRequests);
            trackResponseList = musicClient.addMultiTrack(trackFiles,trackRequestJson);
            List<AlbumTrack> albumTracks = trackResponseList.getData().stream().map((trackResponse) ->
                    AlbumTrack.builder()
                            .trackId(trackResponse.getId()).album(newAlbum)
                            .addedAt(LocalDateTime.now()).build()).toList();
            newAlbum.setTracks(albumTracks);
        }
        List<String> tagsId = request.getTagsId();
        List<AlbumTag> albumTags = tagsId.stream()
                .map(id -> AlbumTag.builder()
                        .album(newAlbum)
                        .tagId(id)
                        .build())
                .collect(Collectors.toList());
        newAlbum.setTags(albumTags);
        albumRepository.save(newAlbum);
        this.kafkaService.sendAlbumToSearchService(newAlbum);
        AlbumResponse response = albumMapper.toAlbumResponse(newAlbum);
        if (trackResponseList != null && !trackResponseList.getData().isEmpty()) {
            response.setTracks(trackResponseList.getData());
        }
        return response;
    }

    @PostAuthorize("returnObject.privacy == 'public' or returnObject.userId == authentication.name")
    public AlbumResponse getAlbumById(String albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
        AlbumResponse response = getFullAlbumResponse(album);
        return response;
    }

    @PostAuthorize("returnObject.privacy == 'public' or returnObject.userId == authentication.name")
    public AlbumResponse getAlbumByLink(String albumLink) {
        Album album = albumRepository.findByAlbumLink(albumLink)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
        AlbumResponse response = getFullAlbumResponse(album);
        return response;
    }

    public List<AlbumResponse> getAlbumByUserId(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggingUserId = (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken)
                ? null
                : authentication.getName();


        List<Album> albums;
        if (loggingUserId == null || !loggingUserId.equals(userId)) {
            // If not logged in or viewing someone else's albums, only get public albums
            albums = albumRepository.findByUserIdAndPrivacy(userId, "public");
        } else {
            // If viewing own albums, get all (public + private)
            albums = albumRepository.findByUserId(userId);
        }

        return albums.stream().map(album -> {
            AlbumResponse response = getFullAlbumResponse(album);
            return response;
        }).toList();
    }


    public List<AlbumResponse> getByIds(List<String> ids) {
        List<Album> albums = this.albumRepository.findAllById(ids);

        return albums.stream().map(album -> {
            AlbumResponse response = getFullAlbumResponse(album);
            return response;
        }).toList();
    }

    @Transactional
    public void deleteAlbumById(String albumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_AMIN"));
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
        if (!Objects.equals(userId, album.getUserId()) && !isAdmin) {
//            Do not allow to delete if user is not owner's album or admin role
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String imagePath = album.getImagePath();
        if (imagePath != null) {
            String name = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            log.info(name);
            fileClient.deleteCoverImage(name);
        }
        album.getTracks().clear();
        albumRepository.save(album);
        albumRepository.delete(album);
        this.kafkaService.deleteAlbumFromSearchService(albumId);
    }

    @Transactional
    public AlbumResponse editAlbum(AlbumRequest request, MultipartFile file, String albumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        if (!Objects.equals(userId, album.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        albumMapper.updateAlbumFromRequest(request, album);

        List<String> albumTagIds = request.getTagsId();
        if (!albumTagIds.isEmpty()) {
            List<AlbumTag> albumTags = new ArrayList<>();

            for (String tagId : albumTagIds) {
                albumTags.add(AlbumTag.builder()
                        .album(album)
                        .tagId(tagId)
                        .build());
            }
            album.getTags().clear();
            album.getTags().addAll(albumTags);
        }

        if (file != null && !file.isEmpty()) {
            String imagePath = album.getImagePath();
            ApiResponse<AddCoverFileResponse> fileResponse;
            if (imagePath!=null) {
                fileResponse = fileClient.replaceCover(file, imagePath);
            } else {
                fileResponse = fileClient.addCover(file);}
            album.setImagePath(fileResponse.getData().getCoverName());
        }

        album = albumRepository.save(album);
        this.kafkaService.sendUpdatedAlbumToSearchService(album);
        return getFullAlbumResponse(album);
    }

    public void likeAlbum(String albumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
        if (likedAlbumRepository.existsByUserIdAndAlbumId(userId, albumId)) {
            throw new AppException(ErrorCode.ALBUM_ALREADY_LIKED);
        }
        LikedAlbum likedAlbum = LikedAlbum.builder().userId(userId).album(album).likedAt(LocalDateTime.now()).build();
        likedAlbumRepository.save(likedAlbum);
    }

    public List<AlbumResponse> getLikedAlbums(String userId) {
        List<LikedAlbum> likedAlbums = likedAlbumRepository.findByUserIdOrderByLikedAtDesc(userId);
        List<Album> albums = likedAlbums.stream()
                .map(LikedAlbum::getAlbum)
                .toList();

        List<AlbumResponse> albumResponses = new ArrayList<>();

        for (Album album : albums) {
            AlbumResponse response = getFullAlbumResponse(album);

            albumResponses.add(response);
        }

        return albumResponses;
    }

    public void unLikeAlbum(String albumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        LikedAlbum likedAlbum = likedAlbumRepository.findByUserIdAndAlbumId(userId, albumId).orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_LIKED));  // Handle if the album is not liked

        likedAlbumRepository.delete(likedAlbum);
    }

    public List<AlbumResponse> getCreatedAndLikedAlbum(String userId) {
        List<Album> albums = albumRepository.findCreatedAndLikedAlbums(userId);
        List<AlbumResponse> albumResponses = new ArrayList<>();

        for (Album album: albums) {
            AlbumResponse response = getFullAlbumResponse(album);

            albumResponses.add(response);
        }
        return albumResponses;
    }
    public void addTrackToAlbum(AddTrackAlbumRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Album album = albumRepository.findById(request.getAlbumId()).orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
        if (!Objects.equals(userId, album.getUserId())) throw new AppException(ErrorCode.UNAUTHORIZED);
        AlbumTrack albumTrack = AlbumTrack.builder().album(album).trackId(request.getTrackId()).addedAt(LocalDateTime.now()).build();
        albumTrackRepository.save(albumTrack);
    }
}
