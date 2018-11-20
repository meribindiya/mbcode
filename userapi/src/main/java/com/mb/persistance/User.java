package com.mb.persistance;

import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "tbl_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	private String email;
	private Long mobile;
	private String password;
	private String name;
	private Date dob;
	private String gender;
	private String referredby;
	private String referenceid;
	private Boolean status;
	private Timestamp createdat;
	private Timestamp updatedat;
	private Integer walletmin;

	@Column(columnDefinition = "bit default 0")
	private boolean isActivated; // set to true if made any purchase

    @Column(columnDefinition = "bit default 0")
	private boolean isBeautyNetworkActivated;// set to true if any of the direct child made any purchase.

	@Column(columnDefinition = "bit default 1")
	private boolean isAppAlive = true;

	@CreationTimestamp
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp appAliveUpdatedAt;

	private String location;

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getReferredby() {
		return referredby;
	}

	public void setReferredby(String referredby) {
		this.referredby = referredby;
	}

	public String getReferenceid() {
		return referenceid;
	}

	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Timestamp getCreatedat() {
		return createdat;
	}

	public void setCreatedat(Timestamp createdat) {
		this.createdat = createdat;
	}

	public Timestamp getUpdatedat() {
		return updatedat;
	}

	public void setUpdatedat(Timestamp updatedat) {
		this.updatedat = updatedat;
	}

	public Integer getWalletmin() {
		return walletmin;
	}

	public void setWalletmin(Integer walletmin) {
		this.walletmin = walletmin;
	}

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public boolean isBeautyNetworkActivated() {
        return isBeautyNetworkActivated;
    }

    public void setBeautyNetworkActivated(boolean beautyNetworkActivated) {
        isBeautyNetworkActivated = beautyNetworkActivated;
    }

	public boolean isAppAlive() {
		return isAppAlive;
	}

	public void setAppAlive(boolean appAlive) {
		isAppAlive = appAlive;
	}

	public Timestamp getAppAliveUpdatedAt() {
		return appAliveUpdatedAt;
	}

	public void setAppAliveUpdatedAt(Timestamp appAliveUpdatedAt) {
		this.appAliveUpdatedAt = appAliveUpdatedAt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "User{" +
				"user_id=" + user_id +
				", email='" + email + '\'' +
				", mobile=" + mobile +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", dob=" + dob +
				", gender='" + gender + '\'' +
				", referredby='" + referredby + '\'' +
				", referenceid='" + referenceid + '\'' +
				", status=" + status +
				", createdat=" + createdat +
				", updatedat=" + updatedat +
				", walletmin=" + walletmin +
				", isActivated=" + isActivated +
				", isBeautyNetworkActivated=" + isBeautyNetworkActivated +
				", isAppAlive=" + isAppAlive +
				", appAliveUpdatedAt=" + appAliveUpdatedAt +
				", location='" + location + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(mobile, user.mobile);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mobile);
	}
}
