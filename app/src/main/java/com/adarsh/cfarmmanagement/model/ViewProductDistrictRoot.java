package com.adarsh.cfarmmanagement.model;

import java.util.ArrayList;
import java.util.List;

public class ViewProductDistrictRoot {
    private String status;

    private List<ViewProductDistrict> Details;

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setDetails(List<ViewProductDistrict> Details){
        this.Details = Details;
    }
    public List<ViewProductDistrict> getDetails(){
        return this.Details;
    }
}
