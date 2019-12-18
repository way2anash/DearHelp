package com.dearhelp.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DearHelpUsers")
public class DearHelpUsers {

	@Id
	//basics info
	private String id;
	private String userName;
	private String email;
	private String password;
	private String cnfPassword;
	private String role;
	private String phoneNumber;
	private String imageUrl;
	private String resetToken;
	
	//additional sp info
	private String businessName;
	private String serviceType;
	private String specialization;
	private String aboutUs;
	private String workingTime;
	private String closingDay;
	
	//address components
	private Address address;
	
	//contact us info
	private ArrayList<Messages> messages;
	
	//rating and reviews components
	private ArrayList<Integer> ratingNumbers;
	private String rating;
	private ArrayList<Reviews> reviews;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCnfPassword() {
		return cnfPassword;
	}
	public void setCnfPassword(String cnfPassword) {
		this.cnfPassword = cnfPassword;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getResetToken() {
		return resetToken;
	}
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getAboutUs() {
		return aboutUs;
	}
	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}
	public String getWorkingTime() {
		return workingTime;
	}
	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}
	public String getClosingDay() {
		return closingDay;
	}
	public void setClosingDay(String closingDay) {
		this.closingDay = closingDay;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public ArrayList<Integer> getRatingNumbers() {
		return ratingNumbers;
	}
	public void setRatingNumbers(ArrayList<Integer> ratingNumbers) {
		this.ratingNumbers = ratingNumbers;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public ArrayList<Reviews> getReviews() {
		return reviews;
	}
	public void setReviews(ArrayList<Reviews> reviews) {
		this.reviews = reviews;
	}
	
	
	public ArrayList<Messages> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<Messages> messages) {
		this.messages = messages;
	}
	@Override
	public String toString() {
		return "DearHelpUsers [id=" + id + ", userName=" + userName + ", email=" + email + ", role=" + role
				+ ", phoneNumber=" + phoneNumber + ", imageUrl=" + imageUrl + ", resetToken=" + resetToken
				+ ", businessName=" + businessName + ", serviceType=" + serviceType + ", specialization="
				+ specialization + ", aboutUs=" + aboutUs + ", workingTime=" + workingTime + ", closingDay="
				+ closingDay + ", address=" + address + ", ratingNumbers=" + ratingNumbers + ", rating=" + rating
				+ ", reviews=" + reviews + "]";
	}
	
	
	
}
