package com.MusicPlatForm.user_library_service.service;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AlbumRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.client.TrackRequest;
import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.CoverRequest;
import com.MusicPlatForm.user_library_service.dto.response.album.AlbumResponse;
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
import com.MusicPlatForm.user_library_service.repository.AlbumTagRepository;
import com.MusicPlatForm.user_library_service.repository.AlbumTrackRepository;
import com.MusicPlatForm.user_library_service.repository.LikedAlbumRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Track;
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
    AlbumTagRepository albumTagRepository;
    LikedAlbumRepository likedAlbumRepository;
    AlbumMapper albumMapper;
    FileClient fileClient;
    MusicClient musicClient;
    @Value("${app.services.file}")
    @NonFinal
    String fileServiceUrl;
    @Transactional
    public AlbumResponse addAlbum(AlbumRequest request, MultipartFile coverAlbum, List<MultipartFile> trackFiles, List<TrackRequest> trackRequests) throws JsonProcessingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Album newAlbum = albumMapper.toAlbum(request);
        ApiResponse<List<TrackResponse>> trackResponseList = null;
        newAlbum.setUserId(userId);
        if (coverAlbum != null && !coverAlbum.isEmpty()) {
            ApiResponse<AddCoverFileResponse> coverFileResponse = fileClient.addCover(coverAlbum);
            String nameImage = coverFileResponse.getData().getCoverName();
            newAlbum.setImagePath(fileServiceUrl + "/image/" + "cover/" + nameImage);
        if (trackFiles != null && !trackFiles.isEmpty() && trackRequests != null && !trackRequests.isEmpty()) {
        }
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
        AlbumResponse response = albumMapper.toAlbumResponse(newAlbum);
        if (trackResponseList != null && !trackResponseList.getData().isEmpty()) {
            response.setTrackResponses(trackResponseList.getData());
        }
        return response;
    }

    @PostAuthorize("returnObject.privacy == 'public' or returnObject.userId == authentication.name")
    public AlbumResponse getAlbumById(String albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
        AlbumResponse response = albumMapper.toAlbumResponse(album);
//        List<AlbumTag> albumTags = albumTagRepository.findAllByAlbum_Id(albumId);
//        if (!albumTags.isEmpty()) {
//            List<String> tags =  albumTags.stream().map(AlbumTag::getTagId).toList();
//            response.setTagsId(tags);
//        }
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
            AlbumResponse response = albumMapper.toAlbumResponse(album);
            response.setTagsId(album.getTags().stream().map(AlbumTag::getTagId).toList());
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
            fileClient.deleteAvatar(CoverRequest.builder().coverName(name).build());
        }
        album.getTracks().clear();
        albumRepository.save(album);
        albumRepository.delete(album);
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
            String name = (imagePath != null && !imagePath.isBlank() && imagePath.contains("/"))
                    ? imagePath.substring(imagePath.lastIndexOf("/") + 1)
                    : file.getOriginalFilename();

            ApiResponse<AddCoverFileResponse> fileResponse = fileClient.replaceCover(file, name);
            if (fileResponse != null && fileResponse.getData() != null) {
                album.setImagePath(fileServiceUrl + "/image/cover/" + fileResponse.getData().getCoverName());
            }
        }

        album = albumRepository.save(album);
        return albumMapper.toAlbumResponse(album);
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
            AlbumResponse response = albumMapper.toAlbumResponse(album);

            // Lấy trackIds từ album
            List<String> trackIds = album.getTracks().stream()
                    .map(AlbumTrack::getTrackId)
                    .toList();
            ApiResponse<List<TrackResponse>> trackResponses = musicClient.getTrackByIds(trackIds);
            response.setTrackResponses(trackResponses.getData());
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
            AlbumResponse response = albumMapper.toAlbumResponse(album);

            List<String> trackIds = album.getTracks().stream()
                    .map(AlbumTrack::getTrackId)
                    .toList();
            ApiResponse<List<TrackResponse>> trackResponses = musicClient.getTrackByIds(trackIds);
            response.setTrackResponses(trackResponses.getData());
            albumResponses.add(response);
        }
        return albumResponses;
    }

}
