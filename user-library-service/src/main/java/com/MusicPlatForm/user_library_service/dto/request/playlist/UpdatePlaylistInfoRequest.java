package com.MusicPlatForm.user_library_service.dto.request.playlist;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdatePlaylistInfoRequest  extends AddPlaylistRequest{
    @NotBlank
    @NotNull
    String id;
    
    @NotNull
    LocalDateTime releaseDate;
    String description;
    String genre;
}
