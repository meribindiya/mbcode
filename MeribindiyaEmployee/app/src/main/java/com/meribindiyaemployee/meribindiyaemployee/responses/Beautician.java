package com.meribindiyaemployee.meribindiyaemployee.responses;

import android.os.Parcel;
import android.os.Parcelable;


public class Beautician implements Parcelable {

    private Integer id;
    private String name;
    private Long mobile;
    private String email;
    private String gender;
    private String password;
    private String address;
    private String city;
    private String state;
    private Boolean status;
    private String categoryIds;
    private String createdat;
    private Integer createdby;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Beautician createFromParcel(Parcel in) {
            return new Beautician(in);
        }

        public Beautician[] newArray(int size) {
            return new Beautician[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public Integer getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Integer createdby) {
        this.createdby = createdby;
    }

    @Override
    public String toString() {
        return "Beautician{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile=" + mobile +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", status=" + status +
                ", categoryIds='" + categoryIds + '\'' +
                ", createdat=" + createdat +
                ", createdby=" + createdby +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.id);
        parcel.writeString(this.name);
        parcel.writeLong(this.mobile);
        parcel.writeString(this.email);
        parcel.writeString(this.gender);
        parcel.writeString(this.password);
        parcel.writeString(this.address);
        parcel.writeString(this.city);
        parcel.writeString(this.state);
        parcel.writeValue(this.status);
        parcel.writeString(this.categoryIds);
        parcel.writeString(this.createdat);
        parcel.writeValue(this.createdby);
    }

    public Beautician(Parcel parcel) {
        this.id = (Integer) parcel.readValue(Integer.class.getClassLoader());
        this.name = parcel.readString();
        this.mobile = parcel.readLong();
        this.email = parcel.readString();
        this.gender = parcel.readString();
        this.password = parcel.readString();
        this.address = parcel.readString();
        this.city = parcel.readString();
        this.state = parcel.readString();
        this.status = (Boolean) parcel.readValue(Boolean.class.getClassLoader());
        this.categoryIds = parcel.readString();
        this.createdat = parcel.readString();
        this.createdby = (Integer) parcel.readValue(Integer.class.getClassLoader());
    }
}
