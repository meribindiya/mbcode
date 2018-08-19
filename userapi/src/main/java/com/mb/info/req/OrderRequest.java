package com.mb.info.req;

import java.util.List;

public class OrderRequest {

	private Long userid;
	private Long mobile;
	private Integer addressid;
	private String booking_date; // dd-MM-yyyy
	private String booking_time;
	private String online_source_id;
	private String online_source_txnid;
	private String online_source_txndate;
	private Integer pymnt_mode; // 0 for cod, 1 for online
	private String pymnt_source; // cod,paytm,online
	private Integer category_id;
	private String payment_source_id;
	private String payment_source_txnid;
	private String payment_source_txndate;
	private String utf1;
	private String utf2;
	private String utf3;
	List<OrderServiceRequest> orderServiceRequests;

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

	public String getBooking_date() {
		return booking_date;
	}

	public void setBooking_date(String booking_date) {
		this.booking_date = booking_date;
	}

	public String getBooking_time() {
		return booking_time;
	}

	public void setBooking_time(String booking_time) {
		this.booking_time = booking_time;
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

	public List<OrderServiceRequest> getOrderServiceRequests() {
		return orderServiceRequests;
	}

	public void setOrderServiceRequests(List<OrderServiceRequest> orderServiceRequests) {
		this.orderServiceRequests = orderServiceRequests;
	}
	

	public String getPayment_source_id() {
		return payment_source_id;
	}

	public void setPayment_source_id(String payment_source_id) {
		this.payment_source_id = payment_source_id;
	}

	public String getPayment_source_txnid() {
		return payment_source_txnid;
	}

	public void setPayment_source_txnid(String payment_source_txnid) {
		this.payment_source_txnid = payment_source_txnid;
	}

	public String getPayment_source_txndate() {
		return payment_source_txndate;
	}

	public void setPayment_source_txndate(String payment_source_txndate) {
		this.payment_source_txndate = payment_source_txndate;
	}

	@Override
	public String toString() {
		return "OrderRequest [userid=" + userid + ", mobile=" + mobile + ", addressid=" + addressid + ", booking_date="
				+ booking_date + ", booking_time=" + booking_time + ", online_source_id=" + online_source_id
				+ ", online_source_txnid=" + online_source_txnid + ", online_source_txndate=" + online_source_txndate
				+ ", pymnt_mode=" + pymnt_mode + ", pymnt_source=" + pymnt_source + ", category_id=" + category_id
				+ ", payment_source_id=" + payment_source_id + ", payment_source_txnid=" + payment_source_txnid
				+ ", payment_source_txndate=" + payment_source_txndate + ", utf1=" + utf1 + ", utf2=" + utf2 + ", utf3="
				+ utf3 + ", orderServiceRequests=" + orderServiceRequests + "]";
	}

	

}
