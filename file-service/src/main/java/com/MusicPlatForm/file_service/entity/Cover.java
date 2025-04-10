package com.MusicPlatForm.file_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cover")
public class Cover {
    @Id
    @Column(name = "File_name")    
    String fileName;

    @Column(name = "User_id")
    String userId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Cover(String fileName, String userId) {
        this.fileName = fileName;
        this.userId = userId;
    }

    public Cover() {
    }

    
}
