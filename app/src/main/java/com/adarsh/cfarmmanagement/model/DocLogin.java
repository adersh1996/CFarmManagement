package com.adarsh.cfarmmanagement.model;

public class DocLogin {
    private String doc_id;

    private String name;

    private String mail;

    private String phone;

    private String address;

    private String password;

    private String qualification;

    private String experience;

    private String profile;

    private String document;

    private int status;

    public void setDoc_id(String doc_id){
        this.doc_id = doc_id;
    }
    public String getDoc_id(){
        return this.doc_id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setMail(String mail){
        this.mail = mail;
    }
    public String getMail(){
        return this.mail;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setQualification(String qualification){
        this.qualification = qualification;
    }
    public String getQualification(){
        return this.qualification;
    }
    public void setExperience(String experience){
        this.experience = experience;
    }
    public String getExperience(){
        return this.experience;
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
