package com.MusicPlatForm.search_service.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;


    @Field(type = FieldType.Text,fielddata = true)
    private String displayName;

    @Field(type = FieldType.Text,fielddata = true)
    private String fullname;


    @NotNull
    @NotBlank(message = "User ID cannot be null or empty")
    @Field(type = FieldType.Keyword,index = false)
    private String userId;
}
