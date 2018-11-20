package com.mb.persistance;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_beauticians")
public class Beautician {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private Timestamp createdat;
	private Integer createdby;

	@Transient
	private List<Integer> catIds;
	
	@Transient
	private String createdByName;
	
	@Transient
	private String categories;

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

	public List<Integer> getCatIds() {
		return catIds;
	}

	public void setCatIds(List<Integer> catIds) {
		this.catIds = catIds;
	}

	public Timestamp getCreatedat() {
		return createdat;
	}

	public void setCreatedat(Timestamp createdat) {
		this.createdat = createdat;
	}

	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	
	

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "Beautician [id=" + id + ", name=" + name + ", mobile=" + mobile + ", email=" + email + ", gender="
				+ gender + ", password=" + password + ", address=" + address + " , city="
				+ city + ", state=" + state + ", status=" + status + ", categoryIds=" + categoryIds + ", createdat="
				+ createdat + ", createdby=" + createdby + ", catIds=" + catIds + ", createdByName=" + createdByName
				+ ", categories=" + categories + "]";
	}

	

}
