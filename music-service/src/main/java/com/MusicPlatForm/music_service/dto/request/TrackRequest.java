package com.MusicPlatForm.music_service.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackRequest{
    private String name;
    private String description;
    private String userId;
    private String privacy;
    private String genreId;
    private List<String> tagIds;
}
