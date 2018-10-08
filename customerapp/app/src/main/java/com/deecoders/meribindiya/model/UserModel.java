package com.deecoders.meribindiya.model;

public class UserModel {

    private String user_id;
    private String email;
    private long mobile;
    private String password;
    private String name;
    private String dob;
    private String gender;
    private String referredby;
    private String referenceid;
    private String status;
    private String createdat;
    private String updatedat;
    private String walletmin;
    private String appAliveUpdatedAt;
    private boolean beautyNetworkActivated;
    private boolean appAlive;
    private boolean activated;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

    public String getWalletmin() {
        return walletmin;
    }

    public void setWalletmin(String walletmin) {
        this.walletmin = walletmin;
    }

    public String getAppAliveUpdatedAt() {
        return appAliveUpdatedAt;
    }

    public void setAppAliveUpdatedAt(String appAliveUpdatedAt) {
        this.appAliveUpdatedAt = appAliveUpdatedAt;
    }

    public boolean isBeautyNetworkActivated() {
        return beautyNetworkActivated;
    }

    public void setBeautyNetworkActivated(boolean beautyNetworkActivated) {
        this.beautyNetworkActivated = beautyNetworkActivated;
    }

    public boolean isAppAlive() {
        return appAlive;
    }

    public void setAppAlive(boolean appAlive) {
        this.appAlive = appAlive;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
