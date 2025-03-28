package com.MusicPlatForm.user_library_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.user_library_service.dto.request.playlist.AddPlaylistRequest;
import com.MusicPlatForm.user_library_service.dto.request.playlist.UpdatePlaylistInfoRequest;
import com.MusicPlatForm.user_library_service.dto.response.AddCoverFileResponse;
import com.MusicPlatForm.user_library_service.dto.response.ApiResponse;
import com.MusicPlatForm.user_library_service.dto.response.playlist.PlaylistResponse;
import com.MusicPlatForm.user_library_service.dto.response.playlist.PlaylistTypeResponse;
import com.MusicPlatForm.user_library_service.entity.LikedPlaylist;
import com.MusicPlatForm.user_library_service.entity.Playlist;
import com.MusicPlatForm.user_library_service.entity.PlaylistTag;
import com.MusicPlatForm.user_library_service.entity.PlaylistTrack;
import com.MusicPlatForm.user_library_service.exception.AppException;
import com.MusicPlatForm.user_library_service.exception.ErrorCode;
import com.MusicPlatForm.user_library_service.httpclient.FileClient;
import com.MusicPlatForm.user_library_service.mapper.Playlist.PlaylistMapper;
import com.MusicPlatForm.user_library_service.mapper.Playlist.PlaylistTagMapper;
import com.MusicPlatForm.user_library_service.mapper.Playlist.PlaylistTrackMapper;
import com.MusicPlatForm.user_library_service.repository.LikedPlaylistRepository;
import com.MusicPlatForm.user_library_service.repository.PlaylistRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private PlaylistMapper playlistMapper;
    private PlaylistTagMapper playlistTagMapper;
    private PlaylistTrackMapper playlistTrackMapper;
    private FileClient fileClient;
    private LikedPlaylistRepository likedPlaylistRepository;

    private List<PlaylistTypeResponse> toPlaylistTypeResponse(List<PlaylistResponse> playlistResponses,String type){
        List<PlaylistTypeResponse> playlistTypeResponses = playlistResponses.stream().map((playlistResponse)->{
            return new PlaylistTypeResponse(playlistResponse,type);
        }).collect(Collectors.toList());
        return playlistTypeResponses;
    }
    //done
    public ApiResponse<List<PlaylistTypeResponse>> getPlaylists(){
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        

        List<Playlist> createdPlaylists = playlistRepository.getPlaylists(userId);
        List<PlaylistTypeResponse> createdPlaylistResponse = toPlaylistTypeResponse(playlistMapper.toPlaylistResponses(createdPlaylists),"Created");
        
        List<Playlist> likedPlaylists = this.likedPlaylistRepository.findAllByUserId(userId).stream().map((likedPlaylist)->likedPlaylist.getPlaylist()).toList();
        List<PlaylistTypeResponse> likedPlaylistResponse = toPlaylistTypeResponse(playlistMapper.toPlaylistResponses(likedPlaylists),"Liked");

        createdPlaylistResponse.addAll(likedPlaylistResponse);
        List<PlaylistTypeResponse> all = createdPlaylistResponse;
        all.sort((e1,e2)-> e2.getPlaylistResponse().getCreatedAt().compareTo(e1.getPlaylistResponse().getCreatedAt()));

        return ApiResponse.<List<PlaylistTypeResponse>>builder()
                                            .data(all)
                                            .code(200)
                                            .message("Playlists for user")
                                            .build();
    }
    //done
    public ApiResponse<PlaylistResponse> getPlaylistById(String id){
        Playlist playlist = this.playlistRepository.findById(id)
                    .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
        PlaylistResponse playlistResponse = this.playlistMapper.toPlaylistResponse(playlist);
        return ApiResponse.<PlaylistResponse>builder()
                                .data(playlistResponse)
                                .code(200)
                                .build();
    }

    //done
    @Transactional
    public PlaylistResponse addPlaylist(AddPlaylistRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        if(userId==null){
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        Playlist playlist = playlistMapper.toPlaylist(request);
        
        List<PlaylistTrack>playlistTracks = playlistTrackMapper.toPlaylistTracks(request.getTracks());
        AtomicInteger index = new AtomicInteger(0);
        playlistTracks.forEach(track->{
            track.setPlaylist(playlist);
            track.setAddedAt(LocalDateTime.now().plusNanos(index.getAndIncrement() * 1000));
        });
        playlist.setPlaylistTracks(playlistTracks);
        playlist.setUserId(userId);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        PlaylistResponse playlistResponse = this.playlistMapper.toPlaylistResponse(savedPlaylist);
        return playlistResponse;
    }

    //done
    @Transactional
    public PlaylistResponse updatePlaylistInfo(MultipartFile playlistCoverImage,UpdatePlaylistInfoRequest request){

        Playlist playlist = playlistRepository.findById(request.getId())
                                                .orElseThrow(()->new AppException(ErrorCode.PLAYLIST_NOT_FOUND));
        playlistMapper.updatePlaylistFromRequest(request, playlist);
        if(playlistCoverImage!=null){
            if(playlist.getImagePath()!=null&&playlist.getImagePath().length()>0){
                this.fileClient.deleteCoverImage(playlist.getImagePath());
            }
            ApiResponse<AddCoverFileResponse> addPlaylistCoverResponse = fileClient.addCover(playlistCoverImage); 
            playlist.setImagePath(addPlaylistCoverResponse.getData().getCoverName());
        }

        List<PlaylistTrack>updatedTracks = playlistTrackMapper.toPlaylistTracks(request.getTracks());
        updatedTracks.forEach(track->{
            track.setPlaylist(playlist);
        });
        playlist.getPlaylistTracks().removeIf(track ->
                updatedTracks.stream().noneMatch(updated -> updated.getTrackId() != null && updated.getTrackId().equals(track.getTrackId()))
        );
        AtomicInteger index = new AtomicInteger(0);
        
        for (PlaylistTrack updatedTrack : updatedTracks) {
            boolean exists = playlist.getPlaylistTracks().stream()
                    .anyMatch(track -> track.getTrackId() != null && track.getTrackId().equals(updatedTrack.getTrackId()));
            if (!exists) {
                updatedTrack.setAddedAt(LocalDateTime.now().plusNanos(index.getAndIncrement() * 1000));
                playlist.getPlaylistTracks().add(updatedTrack);
            }
        }

        playlist.getPlaylistTags().clear();
        List<PlaylistTag> playlistTags = this.playlistTagMapper.mapTagIdsToPlaylistTags(request.getTagIds());
        playlist.getPlaylistTags().addAll(playlistTags);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        PlaylistResponse playlistResponse = this.playlistMapper.toPlaylistResponse(savedPlaylist);
        return playlistResponse;
    }

    @Transactional
    public void deletePlaylistById(String trackId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        if(userId==null){
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        Playlist playlist = this.playlistRepository.findById(trackId)
                                                    .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));

        if(!playlist.getUserId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if(playlist.getImagePath()!=null&&playlist.getImagePath().length()>0){
            this.fileClient.deleteCoverImage(playlist.getImagePath());
        }
        this.playlistRepository.delete(playlist);
    }
}
