package com.MusicPlatForm.user_library_service.dto.request.playlist;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddPlaylistRequest {
    @NotBlank
    String title;
    @NotBlank
    String privacy;
    List<AddPlaylistTrackRequest> tracks;
}
