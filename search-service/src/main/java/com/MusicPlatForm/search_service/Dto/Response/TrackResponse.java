package com.MusicPlatForm.search_service.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TrackResponse extends SearchResultResponse{
    TrackResponse(){
        super.setType("track");
    }
    private String trackId;
    public void setType(String type){
        super.setType(type); 
    }
    public String getType(){
        return super.getType();
    }
}
