package com.adarsh.cfarmmanagement.model;

import java.util.ArrayList;
import java.util.List;

public class ViewProductFarmRoot {
    private String status;

    private List<ViewProductFarm> Details;

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setDetails(List<ViewProductFarm> Details){
        this.Details = Details;
    }
    public List<ViewProductFarm> getDetails(){
        return this.Details;
    }
}
