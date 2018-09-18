package com.deecoders.meribindiya.model;

import java.io.Serializable;

public class SubCategoryModel implements Serializable{

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("createdby")
    private int createdby;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("createdat")
    private String createdat;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("status")
    private boolean status;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("name")
    private String name;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("catid")
    private int catid;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
