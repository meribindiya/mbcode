package com.mb.info.req;

import java.util.TreeMap;

public class CheckSumValidateRequest {

	private Long userId;
	private Long orderId;
	private Long finalAmount;
	private String email;
	private Long mobile;
	private TreeMap<String, String> paramMap;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Long finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public TreeMap<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(TreeMap<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	@Override
	public String toString() {
		return "CheckSumValidateRequest [userId=" + userId + ", orderId=" + orderId + ", finalAmount=" + finalAmount
				+ ", email=" + email + ", mobile=" + mobile + ", paramMap=" + paramMap + "]";
	}

}
