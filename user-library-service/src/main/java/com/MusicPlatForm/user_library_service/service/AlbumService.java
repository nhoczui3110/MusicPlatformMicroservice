package com.MusicPlatForm.user_library_service.service;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AlbumRequest;
import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.CoverRequest;
import com.MusicPlatForm.user_library_service.dto.response.album.AlbumResponse;
import com.MusicPlatForm.user_library_service.entity.Album;
import com.MusicPlatForm.user_library_service.entity.AlbumTag;
import com.MusicPlatForm.user_library_service.exception.AppException;
import com.MusicPlatForm.user_library_service.exception.ErrorCode;
import com.MusicPlatForm.user_library_service.httpclient.FileClient;
import com.MusicPlatForm.user_library_service.mapper.Playlist.AlbumMapper;
import com.MusicPlatForm.user_library_service.repository.AlbumRepository;
import com.MusicPlatForm.user_library_service.repository.AlbumTagRepository;
import com.MusicPlatForm.user_library_service.repository.AlbumTrackRepository;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumService {
    AlbumRepository albumRepository;
    AlbumTrackRepository albumTrackRepository;
    AlbumTagRepository albumTagRepository;
    AlbumMapper albumMapper;
    FileClient fileClient;
    @Value("${app.services.file}")
    @NonFinal
    String fileServiceUrl;
    @Transactional
    public AlbumResponse addAlbum(AlbumRequest request, MultipartFile coverAlbum) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Album newAlbum = albumMapper.toAlbum(request);
        newAlbum.setUserId(userId);
        if (coverAlbum != null && !coverAlbum.isEmpty()) {
            ApiResponse<AddCoverFileResponse> coverFileResponse = fileClient.addCover(coverAlbum);
            String nameImage = coverFileResponse.getData().getCoverName();
            newAlbum.setImagePath(fileServiceUrl + "/image/" + "cover/" + nameImage);
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

    public Page<AlbumResponse> getAlbumByUserId(int page, int size, String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggingUserId = (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken)
                ? null
                : authentication.getName();

        Pageable pageable = PageRequest.of(page, size);

        Page<Album> albums;
        if (loggingUserId == null || !loggingUserId.equals(userId)) {
            // If not logged in or viewing someone else's albums, only get public albums
            albums = albumRepository.findByUserIdAndPrivacy(pageable, userId, "public");
        } else {
            // If viewing own albums, get all (public + private)
            albums = albumRepository.findByUserId(pageable, userId);
        }

        return albums.map(album -> {
            AlbumResponse response = albumMapper.toAlbumResponse(album);
            response.setTagsId(album.getTags().stream().map(AlbumTag::getTagId).toList());
            return response;
        });
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


}
