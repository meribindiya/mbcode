package com.mb.persistance;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_service")
public class Service {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer catid;
	private Integer subcatid;
	private String name;
	private Integer price;
	private String shortdesc;
	private Integer time;
	private Boolean status;
	private Timestamp createdat;
	private Integer createdby;
	private Integer updatedby;
	private Timestamp updatedat;

	@Transient
	private String createdByName;

	@Transient
	private String updatedByName;
	@Transient
	private String categoryName;
	@Transient
	private String subcategoryName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCatid() {
		return catid;
	}

	public void setCatid(Integer catid) {
		this.catid = catid;
	}

	public Integer getSubcatid() {
		return subcatid;
	}

	public void setSubcatid(Integer subcatid) {
		this.subcatid = subcatid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getShortdesc() {
		return shortdesc;
	}

	public void setShortdesc(String shortdesc) {
		this.shortdesc = shortdesc;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
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

	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Integer getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(Integer updatedby) {
		this.updatedby = updatedby;
	}

	public Timestamp getUpdatedat() {
		return updatedat;
	}

	public void setUpdatedat(Timestamp updatedat) {
		this.updatedat = updatedat;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubcategoryName() {
		return subcategoryName;
	}

	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}

	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	@Override
	public String toString() {
		return "Service [id=" + id + ", catid=" + catid + ", subcatid=" + subcatid + ", name=" + name + ", price="
				+ price + ", shortdesc=" + shortdesc + ", time=" + time + ", status=" + status + ", createdat="
				+ createdat + ", createdby=" + createdby + ", updatedby=" + updatedby + ", updatedat=" + updatedat
				+ ", createdByName=" + createdByName + ", updatedByName=" + updatedByName + ", categoryName="
				+ categoryName + ", subcategoryName=" + subcategoryName + "]";
	}

}
