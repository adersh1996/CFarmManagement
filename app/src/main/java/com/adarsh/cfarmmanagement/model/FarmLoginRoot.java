package com.adarsh.cfarmmanagement.model;

public class FarmLoginRoot {
    private String status;

    private FarmLogin User_data;

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setUser_data(FarmLogin User_data){
        this.User_data = User_data;
    }
    public FarmLogin getUser_data(){
        return this.User_data;
    }
}
