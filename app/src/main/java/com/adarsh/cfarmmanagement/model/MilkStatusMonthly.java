package com.adarsh.cfarmmanagement.model;

public class MilkStatusMonthly {
    private int produced;

    private int spoiled;

    public void setProduced(int produced){
        this.produced = produced;
    }
    public int getProduced(){
        return this.produced;
    }
    public void setSpoiled(int spoiled){
        this.spoiled = spoiled;
    }
    public int getSpoiled(){
        return this.spoiled;
    }
}
