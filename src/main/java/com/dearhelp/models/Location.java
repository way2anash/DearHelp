package com.dearhelp.models;

public class Location {

   private Float longitude;
	
	private Float lattitude;

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLattitude() {
		return lattitude;
	}

	public void setLattitude(Float lattitude) {
		this.lattitude = lattitude;
	}

	@Override
	public String toString() {
		return "Location [longitude=" + longitude + ", lattitude=" + lattitude + "]";
	}
	
	
	
}
