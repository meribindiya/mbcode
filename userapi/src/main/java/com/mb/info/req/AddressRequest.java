package com.mb.info.req;

public class AddressRequest {

	private Long userid;
	private String buildingName;
	private String address;
	private String addressType;
	private String landmark;

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	@Override
	public String toString() {
		return "AddressRequest [userid=" + userid + ", buildingName=" + buildingName + ", address=" + address
				+ ", addressType=" + addressType + ", landmark=" + landmark + "]";
	}

}
