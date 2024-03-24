package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_Location")
public class Location {

	@Id
	String locationId;
	String latitude;
	String longitude;
	String phoneNumber;
	String locationName;
	String locationDescrition;
	String status;
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationDescrition() {
		return locationDescrition;
	}
	public void setLocationDescrition(String locationDescrition) {
		this.locationDescrition = locationDescrition;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Location() {
		
	}
	public Location(String locationId, String latitude, String longitude, String phoneNumber, String locationName,
			String locationDescrition, String status) {
		super();
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.phoneNumber = phoneNumber;
		this.locationName = locationName;
		this.locationDescrition = locationDescrition;
		this.status = status;
	}
	@Override
	public String toString() {
		return "Location [locationId=" + locationId + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", phoneNumber=" + phoneNumber + ", locationName=" + locationName + ", locationDescrition="
				+ locationDescrition + ", status=" + status + "]";
	}
	
}
