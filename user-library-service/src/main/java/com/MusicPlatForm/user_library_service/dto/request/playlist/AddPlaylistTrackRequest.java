package com.MusicPlatForm.user_library_service.dto.request.playlist;

import java.time.LocalDateTime;

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
public class AddPlaylistTrackRequest {
    @NotBlank
    @NotNull
    String trackId;
    LocalDateTime addedAt;
}
