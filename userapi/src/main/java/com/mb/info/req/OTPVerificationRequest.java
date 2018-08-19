package com.mb.info.req;

public class OTPVerificationRequest {

	private Long mobile;
	private Integer otp;

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "OTPVerificationRequest [mobile=" + mobile + ", otp=" + otp + "]";
	}

}
