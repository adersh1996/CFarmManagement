package com.adarsh.cfarmmanagement.model;

public class ViewProductFarm {
    private String id;

    private String farm_id;

    private String product;

    private String price;

    private String quantity;

    private String ingredients;

    private String exp_date;

    private String district;

    private String file;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setFarm_id(String farm_id){
        this.farm_id = farm_id;
    }
    public String getFarm_id(){
        return this.farm_id;
    }
    public void setProduct(String product){
        this.product = product;
    }
    public String getProduct(){
        return this.product;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return this.price;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
    public String getQuantity(){
        return this.quantity;
    }
    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }
    public String getIngredients(){
        return this.ingredients;
    }
    public void setExp_date(String exp_date){
        this.exp_date = exp_date;
    }
    public String getExp_date(){
        return this.exp_date;
    }
    public void setDistrict(String district){
        this.district = district;
    }
    public String getDistrict(){
        return this.district;
    }
    public void setFile(String file){
        this.file = file;
    }
    public String getFile(){
        return this.file;
    }
}
