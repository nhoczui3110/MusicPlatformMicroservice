package com.MusicPlatForm.search_service.Dto.Response;

public abstract class SearchResultResponse {
    private String type;
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
}
