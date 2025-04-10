package com.MusicPlatForm.file_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Audio")
public class Audio {
    @Id
    @Column(name = "File_name")    
    String fileName;

    @Column(name = "Duration")
    String duration;

    @Column(name = "User_id")
    String userId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Audio(String fileName, String duration, String userId) {
        this.fileName = fileName;
        this.duration = duration;
        this.userId = userId;
    }

    public Audio() {
    }
    
}
