package com.dearhelp.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class Messages {

	private String name;
	private String emailId;
	
	private String queries;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date createdDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getQueries() {
		return queries;
	}
	public void setQueries(String queries) {
		this.queries = queries;
	}
	
	
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "Messages [name=" + name + ", emailId=" + emailId + ", queries=" + queries + ", createdDate="
				+ createdDate + "]";
	}
	

	
}
