package com.MusicPlatForm.file_service.rescontroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.dto.ApiResponse;
import com.MusicPlatForm.file_service.dto.response.AudioResponse;
import com.MusicPlatForm.file_service.service.MusicService;

@RestController
@RequestMapping("/audios")
public class MusicRestController {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private MusicService musicService;
    
    public MusicRestController(MusicService musicService){
        this.musicService = musicService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getAudio(@PathVariable String filename) throws IOException {
        Path path = Paths.get(uploadDir).resolve("musics").resolve(filename);

        Resource resource = new UrlResource(path.toUri());
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "audio/mpeg"; // default mp3
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AudioResponse>> addAudio(@RequestPart MultipartFile audio) throws Exception, IOException {
        AudioResponse audioResponse = musicService.addAudio(audio);
        return ResponseEntity.ok()
                            .body(
                                ApiResponse
                                    .<AudioResponse>builder()
                                    .code(200)
                                    .message("Add audio successfully")
                                    .data(audioResponse) // Renamed from 'trackResponse'
                                    .build()
                            );
    }

    @PostMapping("/bulk")
    public ApiResponse<List<AudioResponse>> addAudios(@RequestPart("audioFiles") List<MultipartFile> audioFiles) throws IOException {
        List<AudioResponse> audioResponses = musicService.addAudios(audioFiles);
        return ApiResponse.<List<AudioResponse>>builder()
                                .code(HttpStatus.OK.value())
                                .data(audioResponses)
                                .build();
    }

    @DeleteMapping("/{audioName}")
    public ResponseEntity<ApiResponse<String>> deleteAudio(@PathVariable String audioName) throws IOException, NoSuchFileException {
        musicService.deleteAudio(audioName);
        return ResponseEntity.ok()
                .body(
                    ApiResponse
                        .<String>builder()
                        .code(200)
                        .message("Delete audio successfully") // Renamed from 'track'
                        .build()
                );
    }
}
