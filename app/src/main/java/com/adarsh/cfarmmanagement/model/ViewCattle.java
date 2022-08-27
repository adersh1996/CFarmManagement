package com.adarsh.cfarmmanagement.model;

public class ViewCattle {
    private String id;

    private String tag_id;

    private String farm_id;

    private String gender;

    private String breed;

    private String dob;

    private String weight;

    private String status;

    private String parent_tag;

    private String source;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setTag_id(String tag_id){
        this.tag_id = tag_id;
    }
    public String getTag_id(){
        return this.tag_id;
    }
    public void setFarm_id(String farm_id){
        this.farm_id = farm_id;
    }
    public String getFarm_id(){
        return this.farm_id;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return this.gender;
    }
    public void setBreed(String breed){
        this.breed = breed;
    }
    public String getBreed(){
        return this.breed;
    }
    public void setDob(String dob){
        this.dob = dob;
    }
    public String getDob(){
        return this.dob;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }
    public String getWeight(){
        return this.weight;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setParent_tag(String parent_tag){
        this.parent_tag = parent_tag;
    }
    public String getParent_tag(){
        return this.parent_tag;
    }
    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }
}
