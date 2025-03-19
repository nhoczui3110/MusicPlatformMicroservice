package com.MusicPlatForm.user_library_service.dto.request.playlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlaylistTrackRequest {
    @NotBlank
    @NotNull
    String trackId;
    @NotBlank
    @NotNull
    String playlistId;
}
