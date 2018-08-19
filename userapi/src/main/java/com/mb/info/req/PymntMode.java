package com.mb.info.req;

public enum PymntMode {
	
	COD(0), PREPAID(1);

	private int status;

	PymntMode(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static String findByValue(int value) {
		switch (value) {
		case 0:
			return "CASH AT DELIVERY TIME";
		case 1:
			return "PREPAID";
		default:
			return null;
		}
	}

}
