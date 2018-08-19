package com.mb.persistance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_order_service")
public class CustomerOrderService {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer service_id;
	private Integer service_amount;
	private Integer total_service_amount;
	private Integer minutes;
	private Integer service_status_code;
	private String service_status;
	private Integer quantity;
	
	@Transient
	private String serviceName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getService_id() {
		return service_id;
	}

	public void setService_id(Integer service_id) {
		this.service_id = service_id;
	}

	public Integer getService_amount() {
		return service_amount;
	}

	public void setService_amount(Integer service_amount) {
		this.service_amount = service_amount;
	}

	public Integer getTotal_service_amount() {
		return total_service_amount;
	}

	public void setTotal_service_amount(Integer total_service_amount) {
		this.total_service_amount = total_service_amount;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public Integer getService_status_code() {
		return service_status_code;
	}

	public void setService_status_code(Integer service_status_code) {
		this.service_status_code = service_status_code;
	}

	public String getService_status() {
		return service_status;
	}

	public void setService_status(String service_status) {
		this.service_status = service_status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "CustomerOrderService [id=" + id + ", service_id=" + service_id + ", service_amount=" + service_amount
				+ ", total_service_amount=" + total_service_amount + ", minutes=" + minutes + ", service_status_code="
				+ service_status_code + ", service_status=" + service_status + ", quantity=" + quantity
				+ ", serviceName=" + serviceName + "]";
	}

	

}
