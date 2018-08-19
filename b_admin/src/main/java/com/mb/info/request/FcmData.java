package com.mb.info.request;

public class FcmData {
	private String id;
	private String title;
    private String alert_message;
    private String packageName;
    private String type;
    private String bitmap;
    private String orderId;
    private String longDescription;
    private String udf1;
    private String udf2;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlert_message() {
		return alert_message;
	}
	public void setAlert_message(String alert_message) {
		this.alert_message = alert_message;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBitmap() {
		return bitmap;
	}
	public void setBitmap(String bitmap) {
		this.bitmap = bitmap;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public String getUdf1() {
		return udf1;
	}
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}
	public String getUdf2() {
		return udf2;
	}
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}
	@Override
	public String toString() {
		return "FcmData [id=" + id + ", title=" + title + ", alert_message=" + alert_message + ", packageName="
				+ packageName + ", type=" + type + ", bitmap=" + bitmap + ", orderId=" + orderId + ", longDescription="
				+ longDescription + ", udf1=" + udf1 + ", udf2=" + udf2 + "]";
	}
	
    
}
