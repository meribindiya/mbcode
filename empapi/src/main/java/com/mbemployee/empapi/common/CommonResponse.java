package com.mbemployee.empapi.common;

public class CommonResponse {

	private String status;
	private Object object;
	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CommonResponse [status=" + status + ", object=" + object + ", message=" + message + "]";
	}

}
