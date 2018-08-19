package com.mb.info.request;

public class ChangePasswordRequest {
	
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ChangePassword [password=" + password + "]";
	}
}
