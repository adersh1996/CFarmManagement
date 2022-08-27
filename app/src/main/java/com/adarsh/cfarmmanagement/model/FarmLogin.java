package com.adarsh.cfarmmanagement.model;

public class FarmLogin {
    private String farm_id;

    private String name;

    private String phone;

    private String mailid;

    private String address;

    private String district;

    private String states;

    private String password;

    private String seller;

    private String fssai;

    private String profile;

    private String document;

    private int status;

    public void setFarm_id(String farm_id){
        this.farm_id = farm_id;
    }
    public String getFarm_id(){
        return this.farm_id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setMailid(String mailid){
        this.mailid = mailid;
    }
    public String getMailid(){
        return this.mailid;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setDist(String dist){
        this.district = dist;
    }
    public String getDist(){
        return this.district;
    }
    public void setStates(String states){
        this.states = states;
    }
    public String getStates(){
        return this.states;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setSeller(String seller){
        this.seller = seller;
    }
    public String getSeller(){
        return this.seller;
    }
    public void setFssai(String fssai){
        this.fssai = fssai;
    }
    public String getFssai(){
        return this.fssai;
    }
    public void setProfile(String profile){
        this.profile = profile;
    }
    public String getProfile(){
        return this.profile;
    }
    public void setDocument(String document){
        this.document = document;
    }
    public String getDocument(){
        return this.document;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
}
