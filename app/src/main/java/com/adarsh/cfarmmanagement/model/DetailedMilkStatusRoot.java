package com.adarsh.cfarmmanagement.model;

import java.util.List;

public class DetailedMilkStatusRoot {
    private boolean status;

    private String message;

    private List<DetailedMilkStatus> Milk_details;

    public void setStatus(boolean status){
        this.status = status;
    }
    public boolean getStatus(){
        return this.status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setMilk_details(List<DetailedMilkStatus> Milk_details){
        this.Milk_details = Milk_details;
    }
    public List<DetailedMilkStatus> getMilk_details(){
        return this.Milk_details;
    }
}
