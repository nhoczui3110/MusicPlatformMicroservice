package com.MusicPlatForm.user_library_service.service;

import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.client.TrackResponse;
import com.MusicPlatForm.user_library_service.dto.response.liketrack.LikedTrackResponse;
import com.MusicPlatForm.user_library_service.entity.LikedTrack;
import com.MusicPlatForm.user_library_service.exception.AppException;
import com.MusicPlatForm.user_library_service.exception.ErrorCode;
import com.MusicPlatForm.user_library_service.httpclient.MusicClient;
import com.MusicPlatForm.user_library_service.mapper.Playlist.LikedTrackMapper;
import com.MusicPlatForm.user_library_service.repository.LikedTrackRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class LikedTrackService {
    LikedTrackRepository likedTrackRepository;
    MusicClient musicClient;
    LikedTrackMapper likedTrackMapper;

    public List<LikedTrack> getLikedTrack(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<LikedTrack> likedTracks = likedTrackRepository.findAllByUserId(userId);
        return likedTracks;
    }
    public List<TrackResponse> getAllLikedTracks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<LikedTrack> likedTracks = likedTrackRepository.findAllByUserId(userId);
        List<String> trackIds = likedTracks.stream().map(LikedTrack::getTrackId).collect(Collectors.toList());

        if (trackIds.isEmpty()) {
            return Collections.emptyList();
        }

        ApiResponse<List<TrackResponse>> response = musicClient.getTrackByIds(trackIds);
        response.getData().forEach(tr->tr.setIsLiked(true));
        return response.getData();
    }

    public int getTrackLikeCount(String trackId) {
        return likedTrackRepository.countByTrackId(trackId);
    }

    // @Deprecated
    // public List<LikedTrackResponse> getLikedTrackResponses() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     String userId = authentication.getName();

    //     List<LikedTrack> likedTracks = likedTrackRepository.findAllByUserId(userId);
    //     return likedTrackMapper.toLikedTrackResponses(likedTracks);
    // }

    public Boolean isLiked(String trackId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return likedTrackRepository.findByUserIdAndTrackId(userId, trackId) != null;
    }

    public Boolean likeTrack(String trackId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        TrackResponse track = musicClient.getTrackById(trackId).getData();

        if (track == null) {
            throw new AppException(ErrorCode.TRACK_N0T_FOUND);
        }
        if (track.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (likedTrackRepository.findByUserIdAndTrackId(userId, trackId) != null) {
            throw new AppException(ErrorCode.ALREADY_LIKED);
        }
        LikedTrack likedTrack = new LikedTrack();
        likedTrack.setTrackId(trackId);
        likedTrack.setUserId(userId);

        likedTrackRepository.save(likedTrack);
        return true;
    }
    //unlike theo id
    public Boolean unLikeTrack(String track_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        LikedTrack likedTrack = likedTrackRepository.findByUserIdAndTrackId(userId,track_id);
        if(likedTrack==null) throw new AppException(ErrorCode.NOT_FOUND);
        if (!likedTrack.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        likedTrackRepository.delete(likedTrack);
        return true;
    }
    // unlike theo trackid
//    public Boolean unLikeTrack(String trackId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//
//        LikedTrack likedTrack = likedTrackRepository.findByUserIdAndTrackId(userId, trackId);
//        if (likedTrack == null) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//
//        likedTrackRepository.deleteById(likedTrack.getId());
//        return true;
//    }
}
