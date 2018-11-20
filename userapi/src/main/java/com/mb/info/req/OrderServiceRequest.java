package com.mb.info.req;

public class OrderServiceRequest {

	private Integer serviceid;
	private Integer quantity;

	public Integer getServiceid() {
		return serviceid;
	}

	public void setServiceid(Integer serviceid) {
		this.serviceid = serviceid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderServiceRequest [serviceid=" + serviceid + ", quantity=" + quantity + "]";
	}

}
