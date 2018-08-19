package com.mb.info.req;

public enum OrderStatus {
	ORDER_NEW_REQUEST(1), ORDER_ASSIGN(2), ORDER_START(4), ORDER_COMPLETED(3), ORDER_CANCELLED(10), ORDER_REWORK(5);

	private int status;

	OrderStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static String findByValue(int value) {
		switch (value) {
		case 1:
			return "NEW ORDER";
		case 2:
			return "ASSIGN TO BEAUTICIAN";
		case 4:
			return "STARTED";
		case 5:
			return "ORDER REWORK";
		case 3:
			return "COMPLETED";
		case 10:
			return "CANCELLED";
		default:
			return null;
		}
	}
}
