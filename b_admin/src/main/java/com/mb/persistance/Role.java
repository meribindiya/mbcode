package com.mb.persistance;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_roles")
public class Role {
	@Id
	private Long id;
	private String role;
	private String roledesc;
	private Long createdby;
	private Timestamp createdat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoledesc() {
		return roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	public Long getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}

	public Timestamp getCreatedat() {
		return createdat;
	}

	public void setCreatedat(Timestamp createdat) {
		this.createdat = createdat;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + ", roledesc=" + roledesc + ", createdby=" + createdby
				+ ", createdat=" + createdat + "]";
	}

}
