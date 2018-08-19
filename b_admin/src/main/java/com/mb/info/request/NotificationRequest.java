package com.mb.info.request;

import org.springframework.web.multipart.MultipartFile;

public class NotificationRequest {
	
	private Integer type;
	private String title;
	private String message;
	private MultipartFile file;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return String.format("NotificationRequest [type=%s, title=%s, message=%s, file=%s]", type, title, message,
				file);
	}
	
	
	

}
