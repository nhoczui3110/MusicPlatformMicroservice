package com.MusicPlatForm.file_service.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Value("${file.avatars-dir}")
    private String avatarDir;
    @Value("${file.covers-dir}")
    private String coversDir;


    private String addImage(MultipartFile image,String folder)throws IOException{
        String name = Instant.now().getEpochSecond() + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(folder).resolve(name);
        Files.write(filePath, image.getBytes());
        return name;
    }
    public String addAvatar(MultipartFile avatar) throws IOException{
        return addImage(avatar,avatarDir);
    }
    public String addcoverImage(MultipartFile coverImage) throws IOException{
       return addImage(coverImage,coversDir);
    }
    

    public Resource getImageResource(String filename,String folder) throws IOException{
        Path path = Paths.get(uploadDir).resolve(folder).resolve(filename);
        byte[] imageBytes = Files.readAllBytes(path);

        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        return resource;
    }
    public Resource getCoverImage(String filename) throws IOException{
        return getImageResource(filename, coversDir);
    }
    public Resource getAvatarImage(String filename) throws IOException{
        return getImageResource(filename, avatarDir);
    }


    /*
     * Delete Image
     * @Param: 
     *  + name: image name
     *  + folder: foler is image stored
     */
    public boolean deleteImage(String name,String folder)throws IOException{
        Path filePath = Paths.get(uploadDir).resolve(folder).resolve(name);

        boolean isDeleted = Files.deleteIfExists(filePath);
        return isDeleted;
    }
    public void deleteAvatar(String avatarName) throws NoSuchFileException, IOException {
        boolean isDeleted = deleteImage(avatarName, avatarDir);
        if(!isDeleted) throw new NoSuchFileException("File not found");
    }
    public void deleteCover(String coverName) throws NoSuchFileException, IOException {
        boolean isDeleted = deleteImage(coverName, coversDir);
        if(!isDeleted) throw new NoSuchFileException("File not found");
    }



    /*
     * Replace Image
     *
     */

    public String repaceImage(MultipartFile image, String name, String folder) throws IOException, NoSuchFileException{
        boolean isDeleted = deleteImage(name, folder);
        if(!isDeleted) throw new NoSuchFileException("Image not found");
        String newName = addImage(image, folder);
        return newName;
    }
    public String replaceAvatar(MultipartFile avatar, String oldAvatarName) throws IOException, NoSuchFileException{
        return repaceImage(avatar, oldAvatarName, avatarDir);
    }
    public String replaceCover(MultipartFile cover, String oldCoverrName) throws IOException, NoSuchFileException{
        return repaceImage(cover, oldCoverrName, coversDir);
    }
}
