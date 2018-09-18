package com.deecoders.meribindiya.model;

import java.io.Serializable;

public class CategoryModel implements Serializable{

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("status")
    private boolean status;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("updatedby")
    private int updatedby;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("updatedat")
    private String updatedat;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("createdby")
    private int createdby;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("createdat")
    private String createdat;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("image")
    private String image;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("name")
    private String name;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
