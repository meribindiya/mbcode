package com.mb.info.request;

public class FcmMain {
	private String to;
	private FcmData data;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public FcmData getData() {
		return data;
	}

	public void setData(FcmData data) {
		this.data = data;
	}



	@Override
	public String toString() {
		return String.format("SpFcmMain [to=%s, data=%s, notification=%s]", to, data);
	}

}
