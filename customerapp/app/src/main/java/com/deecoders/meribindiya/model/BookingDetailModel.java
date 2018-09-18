package com.deecoders.meribindiya.model;

import java.util.ArrayList;

public class BookingDetailModel {
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("bookedat")
    private String bookedat;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("bookingdate")
    private String bookingdate;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("orderstatus")
    private String orderstatus;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("services")
    private String services;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("total")
    private int total;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("pymnt_source")
    private String pymnt_source;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("pymnt_mode")
    private int pymnt_mode;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("mobile")
    private long mobile;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("userid")
    private int userid;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("booking_time")
    private String booking_time;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("customerOrderServices")
    private ArrayList<CustomerOrderServiceModel> customerOrderServices;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("address")
    private AddressModel address;

    public String getBookedat() {
        return bookedat;
    }

    public void setBookedat(String bookedat) {
        this.bookedat = bookedat;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPymnt_source() {
        return pymnt_source;
    }

    public void setPymnt_source(String pymnt_source) {
        this.pymnt_source = pymnt_source;
    }

    public int getPymnt_mode() {
        return pymnt_mode;
    }

    public void setPymnt_mode(int pymnt_mode) {
        this.pymnt_mode = pymnt_mode;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<CustomerOrderServiceModel> getCustomerOrderServices() {
        return customerOrderServices;
    }

    public void setCustomerOrderServices(ArrayList<CustomerOrderServiceModel> customerOrderServices) {
        this.customerOrderServices = customerOrderServices;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }
}
