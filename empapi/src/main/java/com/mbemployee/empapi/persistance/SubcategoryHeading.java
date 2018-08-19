package com.mbemployee.empapi.persistance;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_subcategory")
public class SubcategoryHeading {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer catid;
	private Integer subcatid;
	private String heading;
	private Boolean status;
	private Timestamp createdat;
	private Integer createdby;
	private Integer sort;

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

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "SubHeading [id=" + id + ", catid=" + catid + ", subcatid=" + subcatid + ", heading=" + heading
				+ ", status=" + status + ", createdat=" + createdat + ", createdby=" + createdby + ", sort=" + sort
				+ "]";
	}

}
