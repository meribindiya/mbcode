package com.mb.info.req;

public class UserRequest {

	private Long mobile;
	private String email;
	private String gender;
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserRequest [mobile=" + mobile + ", email=" + email + ", gender=" + gender + ", name=" + name + "]";
	}

}
