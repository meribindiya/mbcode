package com.meribindiyaemployee.meribindiyaemployee.responses;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;


public class CustomerOrder {

    private Long id;

    private Date booking_date;
    private String booking_time;
    private Long userid;
    private Long mobile;
    private Integer addressid;
    private String online_source_id;
    private String online_source_txnid;
    private String online_source_txndate;
    private Integer pymnt_mode; // 0 for cod, 1 for online
    private String pymnt_source; // cod,paytm,online
    private Integer category_id;
    private Integer total;
    private Integer spid;
    private Integer order_status_code;

    private String utf1;

    private String utf2;

    private String utf3;


    private Timestamp booked_at;


    private String services;

    private String orderstatus;

    private String bookingdate;


    private String bookedat;


    private String userName;


    private List<CustomerOrderService> customerOrderServices;


    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(Date booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public Integer getAddressid() {
        return addressid;
    }

    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    public String getOnline_source_id() {
        return online_source_id;
    }

    public void setOnline_source_id(String online_source_id) {
        this.online_source_id = online_source_id;
    }

    public String getOnline_source_txnid() {
        return online_source_txnid;
    }

    public void setOnline_source_txnid(String online_source_txnid) {
        this.online_source_txnid = online_source_txnid;
    }

    public String getOnline_source_txndate() {
        return online_source_txndate;
    }

    public void setOnline_source_txndate(String online_source_txndate) {
        this.online_source_txndate = online_source_txndate;
    }

    public Integer getPymnt_mode() {
        return pymnt_mode;
    }

    public void setPymnt_mode(Integer pymnt_mode) {
        this.pymnt_mode = pymnt_mode;
    }

    public String getPymnt_source() {
        return pymnt_source;
    }

    public void setPymnt_source(String pymnt_source) {
        this.pymnt_source = pymnt_source;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getUtf1() {
        return utf1;
    }

    public void setUtf1(String utf1) {
        this.utf1 = utf1;
    }

    public String getUtf2() {
        return utf2;
    }

    public void setUtf2(String utf2) {
        this.utf2 = utf2;
    }

    public String getUtf3() {
        return utf3;
    }

    public void setUtf3(String utf3) {
        this.utf3 = utf3;
    }


    public List<CustomerOrderService> getCustomerOrderServices() {
        return customerOrderServices;
    }

    public void setCustomerOrderServices(List<CustomerOrderService> customerOrderServices) {
        this.customerOrderServices = customerOrderServices;
    }

    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }

    public Integer getOrder_status_code() {
        return order_status_code;
    }

    public void setOrder_status_code(Integer order_status_code) {
        this.order_status_code = order_status_code;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Timestamp getBooked_at() {
        return booked_at;
    }

    public void setBooked_at(Timestamp booked_at) {
        this.booked_at = booked_at;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getBookedat() {
        return bookedat;
    }

    public void setBookedat(String bookedat) {
        this.bookedat = bookedat;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "CustomerOrder [id=" + id + ", booking_date=" + booking_date + ", booking_time=" + booking_time
                + ", userid=" + userid + ", mobile=" + mobile + ", addressid=" + addressid + ", online_source_id="
                + online_source_id + ", online_source_txnid=" + online_source_txnid + ", online_source_txndate="
                + online_source_txndate + ", pymnt_mode=" + pymnt_mode + ", pymnt_source=" + pymnt_source
                + ", category_id=" + category_id + ", total=" + total + ", spid=" + spid + ", order_status_code="
                + order_status_code + ", utf1=" + utf1 + ", utf2=" + utf2 + ", utf3=" + utf3 + ", booked_at="
                + booked_at + ", services=" + services + ", orderstatus=" + orderstatus + ", bookingdate=" + bookingdate
                + ", bookedat=" + bookedat + ", userName=" + userName + ", customerOrderServices="
                + customerOrderServices + ", address=" + address + "]";
    }

}
