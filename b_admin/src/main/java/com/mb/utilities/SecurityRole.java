package com.mb.utilities;

public class SecurityRole {

	private String role;
	private String role_desc;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	@Override
	public String toString() {
		return "SecurityRole [role=" + role + ", role_desc=" + role_desc + "]";
	}

}
