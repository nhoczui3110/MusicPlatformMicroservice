package com.MusicPlatForm.search_service.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Playlist {
    @Id
    String id;
    
    @Field(type = FieldType.Text)
    String title;

    @Field(type = FieldType.Text)
    String description;

    @Field(type = FieldType.Keyword)
    String playlistId;
}
