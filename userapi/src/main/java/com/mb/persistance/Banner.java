package com.mb.persistance;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_app_banner")
public class Banner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String filepath;
	private String httppath;
	private Timestamp createdat;
	private Integer createdby;
	private Boolean status;
	private Integer sort;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getHttppath() {
		return httppath;
	}

	public void setHttppath(String httppath) {
		this.httppath = httppath;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "Banner [id=" + id + ", filepath=" + filepath + ", httppath=" + httppath + ", createdat=" + createdat
				+ ", createdby=" + createdby + ", status=" + status + ", sort=" + sort + "]";
	}

}
