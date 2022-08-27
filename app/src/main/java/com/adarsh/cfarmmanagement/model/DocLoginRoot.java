package com.adarsh.cfarmmanagement.model;

public class DocLoginRoot {
    private String status;

    private DocLogin User_data;

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setUser_data(DocLogin User_data){
        this.User_data = User_data;
    }
    public DocLogin getUser_data(){
        return this.User_data;
    }
}
