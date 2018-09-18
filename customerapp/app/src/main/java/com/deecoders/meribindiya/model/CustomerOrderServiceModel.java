package com.deecoders.meribindiya.model;

public class CustomerOrderServiceModel {
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("minutes")
    private int minutes;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("total_service_amount")
    private int total_service_amount;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("service_amount")
    private int service_amount;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("service_id")
    private int service_id;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("serviceName")
    private String serviceName;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("quantity")
    private int quantity;


    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getTotal_service_amount() {
        return total_service_amount;
    }

    public void setTotal_service_amount(int total_service_amount) {
        this.total_service_amount = total_service_amount;
    }

    public int getService_amount() {
        return service_amount;
    }

    public void setService_amount(int service_amount) {
        this.service_amount = service_amount;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
