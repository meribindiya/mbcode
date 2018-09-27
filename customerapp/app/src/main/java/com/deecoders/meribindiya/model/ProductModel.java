package com.deecoders.meribindiya.model;

public class ProductModel {
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("updatedat")
    private String updatedat;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("updatedby")
    private int updatedby;
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
    @com.google.gson.annotations.SerializedName("time")
    private int time;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("shortdesc")
    private String shortdesc;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("price")
    private int price;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("name")
    private String name;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("subcatid")
    private int subcatid;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("catid")
    private int catid;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;
    private boolean showCount;
    private int count;
    private String subcatName, scheduleDate;

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isShowCount() {
        return showCount;
    }

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubcatid() {
        return subcatid;
    }

    public void setSubcatid(int subcatid) {
        this.subcatid = subcatid;
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
