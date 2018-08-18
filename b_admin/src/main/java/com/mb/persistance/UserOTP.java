package com.mb.persistance;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_user_otp")
public class UserOTP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long userid;
	private long mobile;
	private int mobileotp;
	private String emailid;
	private int emailotp;
	private Timestamp createdat;
	private Timestamp mobileotpExpiretime;
	private Timestamp emailotpExpiretime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public int getMobileotp() {
		return mobileotp;
	}

	public void setMobileotp(int mobileotp) {
		this.mobileotp = mobileotp;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public int getEmailotp() {
		return emailotp;
	}

	public void setEmailotp(int emailotp) {
		this.emailotp = emailotp;
	}

	public Timestamp getCreatedat() {
		return createdat;
	}

	public void setCreatedat(Timestamp createdat) {
		this.createdat = createdat;
	}

	public Timestamp getMobileotpExpiretime() {
		return mobileotpExpiretime;
	}

	public void setMobileotpExpiretime(Timestamp mobileotpExpiretime) {
		this.mobileotpExpiretime = mobileotpExpiretime;
	}

	public Timestamp getEmailotpExpiretime() {
		return emailotpExpiretime;
	}

	public void setEmailotpExpiretime(Timestamp emailotpExpiretime) {
		this.emailotpExpiretime = emailotpExpiretime;
	}

	@Override
	public String toString() {
		return "UserOTP [id=" + id + ", userid=" + userid + ", mobile=" + mobile + ", mobileotp=" + mobileotp
				+ ", emailid=" + emailid + ", emailotp=" + emailotp + ", createdat=" + createdat
				+ ", mobileotpExpiretime=" + mobileotpExpiretime + ", emailotpExpiretime=" + emailotpExpiretime + "]";
	}

}
