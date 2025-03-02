package com.MusicPlatForm.search_service.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse extends SearchResultResponse{
    UserResponse(){
        super.setType("user");
    }
    private String userId;
    public void setType(String type){
        super.setType(type); 
    }
    public String getType(){
        return super.getType();
    }
}
