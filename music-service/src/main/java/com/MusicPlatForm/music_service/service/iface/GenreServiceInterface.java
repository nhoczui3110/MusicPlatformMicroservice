package com.MusicPlatForm.music_service.service.iface;

import com.MusicPlatForm.music_service.dto.reponse.GenreResponse;
import com.MusicPlatForm.music_service.dto.reponse.TagResponse;
import com.MusicPlatForm.music_service.dto.request.GenreRequest;
import com.MusicPlatForm.music_service.dto.request.TagRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GenreServiceInterface {
    /**
     * Create tag
     * @param request
     */
    public GenreResponse createGenre(GenreRequest request);
    public String deleteGenre(String id);
    public List<GenreResponse> getGenres();
}
