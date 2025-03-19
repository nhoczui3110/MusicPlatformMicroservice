package com.MusicPlatForm.user_library_service.dto.response.playlist;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaylistResponse {
    private String id;
    private String title;
    private LocalDateTime releaseDate;
    private String description;
    private String privacy;
    private String userId;
    private String generId;
    private String imagePath;
    private List<PlaylistTrackResponse> playlistTracks;
}
