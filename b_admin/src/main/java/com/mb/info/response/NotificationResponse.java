package com.mb.info.response;

import java.util.ArrayList;
import java.util.Map;

public class NotificationResponse {
	
	private Long multicast_id;
	private Integer success,failure;
	private String canonical_ids;
	private ArrayList<Map<String,String>> results;
	public Long getMulticast_id() {
		return multicast_id;
	}
	public void setMulticast_id(Long multicast_id) {
		this.multicast_id = multicast_id;
	}
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public Integer getFailure() {
		return failure;
	}
	public void setFailure(Integer failure) {
		this.failure = failure;
	}
	public String getCanonical_ids() {
		return canonical_ids;
	}
	public void setCanonical_ids(String canonical_ids) {
		this.canonical_ids = canonical_ids;
	}
	public ArrayList<Map<String, String>> getResults() {
		return results;
	}
	public void setResults(ArrayList<Map<String, String>> results) {
		this.results = results;
	}
	@Override
	public String toString() {
		return String.format(
				"NotificationResponse [multicast_id=%s, success=%s, failure=%s, canonical_ids=%s, results=%s]",
				multicast_id, success, failure, canonical_ids, results);
	}
	
}
