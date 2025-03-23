package com.MusicPlatForm.user_library_service.dto.response.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LikedPlaylistResponse {
    private String id;
    private String userId;
    PlaylistResponse playlist;
}
