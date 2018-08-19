package com.mb.info.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class BannerRequest {
	
	private List<MultipartFile> files;

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return String.format("BannerRequest [files=%s]", files);
	}
	
	
	
	

}
