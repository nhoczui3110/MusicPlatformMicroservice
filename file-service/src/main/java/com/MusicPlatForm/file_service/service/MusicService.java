package com.MusicPlatForm.file_service.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.dto.response.TrackResponse;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import java.lang.Math;

@Service
public class MusicService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Value("${file.musics-dir}")
    private String musicDir;

    private String getDuration(String filePath) {
          long totalMilliseconds = 0;
          try (FileInputStream fis = new FileInputStream(filePath)) {
              Bitstream bitstream = new Bitstream(fis);
              Header header;
  
              while ((header = bitstream.readFrame()) != null) {
                  totalMilliseconds += header.ms_per_frame();
                  bitstream.closeFrame();
              }
  
              long totalSeconds = (int)Math.ceil(totalMilliseconds/1000.0);
              
              return (totalSeconds/60)+":"+totalSeconds%60;
          } catch (Exception e) {
              e.printStackTrace();
              return "";
          }
    }


    public TrackResponse addTrack(MultipartFile track) throws Exception,IOException{
        String name = Instant.now().getEpochSecond()+ track.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(musicDir).resolve(name);
        Files.write(filePath, track.getBytes());
        String duration = getDuration(filePath.toString());
        return TrackResponse.builder()
                            .duration(duration)
                            .track(name)
                            .build();
    }

    public List<TrackResponse> addTracks(List<MultipartFile> trackFiles) throws IOException{
           List<TrackResponse> trackResponses = new ArrayList<>();
           for(MultipartFile trackFile: trackFiles){
                String name = Instant.now().getEpochSecond() + trackFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir).resolve(musicDir).resolve(name);
                Files.write(filePath, trackFile.getBytes());
                String duration = getDuration(filePath.toString());
                trackResponses.add(TrackResponse.builder()
                                .duration(duration)
                                .track(name)
                                .build());
           }
           return trackResponses;
    }

    public void deleteTrack(String trackName) throws IOException, NoSuchFileException{
        Path filePath = Paths.get(uploadDir).resolve(musicDir).resolve(trackName);
        Boolean isDeleted = Files.deleteIfExists(filePath);
        if(!isDeleted) throw new NoSuchFileException("Track File not found");
    }
}
