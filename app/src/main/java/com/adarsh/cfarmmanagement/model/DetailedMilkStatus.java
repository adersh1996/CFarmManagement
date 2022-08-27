package com.adarsh.cfarmmanagement.model;

public class DetailedMilkStatus {
    private String milk_id;

    private String farm_id;

    private String p_date;

    private String produced;

    private String spoiled;

    public void setMilk_id(String milk_id){
        this.milk_id = milk_id;
    }
    public String getMilk_id(){
        return this.milk_id;
    }
    public void setFarm_id(String farm_id){
        this.farm_id = farm_id;
    }
    public String getFarm_id(){
        return this.farm_id;
    }
    public void setP_date(String p_date){
        this.p_date = p_date;
    }
    public String getP_date(){
        return this.p_date;
    }
    public void setProduced(String produced){
        this.produced = produced;
    }
    public String getProduced(){
        return this.produced;
    }
    public void setSpoiled(String spoiled){
        this.spoiled = spoiled;
    }
    public String getSpoiled(){
        return this.spoiled;
    }
}
