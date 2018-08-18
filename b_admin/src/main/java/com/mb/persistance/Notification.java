package com.mb.persistance;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String notification;
	private Integer type;
	private String notification_image_path;
	private Timestamp start_at;
	private Timestamp finish_at;
	private Long pushed_by;
	private Float success_rate;
	private Float failed_rate;
	private Integer success;
	private Integer failed;
	private Integer total;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public Timestamp getStart_at() {
		return start_at;
	}

	public void setStart_at(Timestamp start_at) {
		this.start_at = start_at;
	}

	public Timestamp getFinish_at() {
		return finish_at;
	}

	public void setFinish_at(Timestamp finish_at) {
		this.finish_at = finish_at;
	}

	public Long getPushed_by() {
		return pushed_by;
	}

	public void setPushed_by(Long pushed_by) {
		this.pushed_by = pushed_by;
	}

	public Float getSuccess_rate() {
		return success_rate;
	}

	public void setSuccess_rate(Float success_rate) {
		this.success_rate = success_rate;
	}

	public Float getFailed_rate() {
		return failed_rate;
	}

	public void setFailed_rate(Float failed_rate) {
		this.failed_rate = failed_rate;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getFailed() {
		return failed;
	}

	public void setFailed(Integer failed) {
		this.failed = failed;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNotification_image_path() {
		return notification_image_path;
	}

	public void setNotification_image_path(String notification_image_path) {
		this.notification_image_path = notification_image_path;
	}

	@Override
	public String toString() {
		return String.format(
				"Notification [id=%s, title=%s, notification=%s, type=%s, notification_image_path=%s, start_at=%s, finish_at=%s, pushed_by=%s, success_rate=%s, failed_rate=%s, success=%s, failed=%s, total=%s]",
				id, title, notification, type, notification_image_path, start_at, finish_at, pushed_by, success_rate,
				failed_rate, success, failed, total);
	}

}
