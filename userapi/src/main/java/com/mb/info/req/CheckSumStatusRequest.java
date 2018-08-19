package com.mb.info.req;



public class CheckSumStatusRequest {
	
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "CheckSumStatusRequest [orderId=" + orderId + "]";
	}

	
	
}
