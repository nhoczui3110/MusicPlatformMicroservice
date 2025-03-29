package com.MusicPlatForm.user_library_service.dto.response.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AlbumResponse {
    String albumTitle;
    String mainArtists;
    String genreId;
    String albumType;
    List<String> tagsId;
    String description;
    String privacy;
    String albumLink;
    String imagePath;
    String userId;
    String id;
}
