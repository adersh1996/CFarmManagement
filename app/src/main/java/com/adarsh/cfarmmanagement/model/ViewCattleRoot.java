package com.adarsh.cfarmmanagement.model;

import java.util.List;

public class ViewCattleRoot {
    private String status;

    private List<ViewCattle> Details;

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setDetails(List<ViewCattle> Details){
        this.Details = Details;
    }
    public List<ViewCattle> getDetails(){
        return this.Details;
    }
}
