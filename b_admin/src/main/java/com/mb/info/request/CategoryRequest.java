package com.mb.info.request;

import org.springframework.web.multipart.MultipartFile;

public class CategoryRequest {

	private Integer id;
	private String name;
	private Boolean status;
	private MultipartFile file;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "CategoryRequest [id=" + id + ", name=" + name + ", status=" + status + ", file=" + file + "]";
	}

}
