package com.MusicPlatForm.user_library_service.dto.request.playlist;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, message = "Track ID must be at least 1 character")
    String trackId;
}
