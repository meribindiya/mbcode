package com.mb.info.req;

public class UserFcmRequest {

	private Long mobile;
	private String fcm;

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getFcm() {
		return fcm;
	}

	public void setFcm(String fcm) {
		this.fcm = fcm;
	}

	@Override
	public String toString() {
		return "UserFcmRequest [mobile=" + mobile + ", fcm=" + fcm + "]";
	}

}
