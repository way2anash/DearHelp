package com.dearhelp.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class Reviews {

	private String email;
	private String review;
	private int ratingValue;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date createdDate;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public int getRatingValue() {
		return ratingValue;
	}
	public void setRatingValue(int ratingValue) {
		this.ratingValue = ratingValue;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	@Override
	public String toString() {
		return "Reviews [email=" + email + ", review=" + review + ", ratingValue=" + ratingValue + ", createdDate="
				+ createdDate + "]";
	}
	
	
	
}
