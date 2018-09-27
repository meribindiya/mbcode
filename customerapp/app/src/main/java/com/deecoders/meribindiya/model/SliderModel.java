package com.deecoders.meribindiya.model;

public class SliderModel {
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("status")
    private boolean status;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("createdby")
    private int createdby;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("createdat")
    private String createdat;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("httppath")
    private String httppath;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("filepath")
    private String filepath;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getHttppath() {
        return httppath;
    }

    public void setHttppath(String httppath) {
        this.httppath = httppath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
