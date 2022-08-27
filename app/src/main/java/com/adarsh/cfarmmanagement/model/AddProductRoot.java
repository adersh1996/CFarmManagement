package com.adarsh.cfarmmanagement.model;

import java.util.List;

public class AddProductRoot {
    private boolean status;

    private String message;

    private List<AddProduct> customer_details;

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
    public void setCustomer_details(List<AddProduct> customer_details){
        this.customer_details = customer_details;
    }
    public List<AddProduct> getCustomer_details(){
        return this.customer_details;
    }
}
