package com.dearhelp.models;


public class Address {
	
	private String streetName;
	private String city;
	private String zipCode;
	private String state;
	private Location geoLocation;
	
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Location getGeoLocation() {
		return geoLocation;
	}
	public void setGeoLocation(Location geoLocation) {
		this.geoLocation = geoLocation;
	}
	
	@Override
	public String toString() {
		return "Address [streetName=" + streetName + ", city=" + city + ", zipCode=" + zipCode + ", state=" + state
				+ ", geoLocation=" + geoLocation + "]";
	}
	
	
	

}
