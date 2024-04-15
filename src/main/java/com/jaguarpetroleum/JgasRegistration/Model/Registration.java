package com.jaguarpetroleum.JgasRegistration.Model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_Registration")
public class Registration {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recordNo")
	Long recordNo;
	@Column(name = "recordDatetime", columnDefinition="DATETIME")
	private LocalDateTime recordDatetime;
	@Column(name = "firstName")
	String firstName;
	@Column(name = "lastName")
	String lastName;
	@Column(name = "phoneNumber")
	String phoneNumber;
	@Column(name = "alternativePhone")
	String alternativePhone;
	@Column(name = "emailAddress")
	String emailAddress;
	@Column(name = "latitude")
	String latitude;
	@Column(name = "longitude")
	String longitude;
	@Column(name = "idNumber")
	String idNumber;
	@Column(name = "isStaff")
	Integer isStaff;
	@Column(name = "staffNumber")
	String staffNumber;
	@Column(name = "homeAddress")
	String homeAddress;
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public Integer getIsStaff() {
		return isStaff;
	}
	public void setIsStaff(Integer isStaff) {
		this.isStaff = isStaff;
	}
	public String getStaffNumber() {
		return staffNumber;
	}
	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public Long getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(Long recordNo) {
		this.recordNo = recordNo;
	}
	public LocalDateTime getRecordDatetime() {
		return recordDatetime;
	}
	public void setRecordDatetime(LocalDateTime recordDatetime) {
		this.recordDatetime = recordDatetime;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAlternativePhone() {
		return alternativePhone;
	}
	public void setAlternativePhone(String alternativePhone) {
		this.alternativePhone = alternativePhone;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	public Registration() {
		
	}
/*
 * 	public Registration(Integer recordNo, Date recordDatetime, String firstName, String lastName, String phoneNumber,
			String alternativePhone, String emailAddress, String latitude, String longitude, String idNumber,
			Integer isStaff, String staffNumber) {
		super();
		this.recordNo = recordNo;
		this.recordDatetime = recordDatetime;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.alternativePhone = alternativePhone;
		this.emailAddress = emailAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.idNumber = idNumber;
		this.isStaff = isStaff;
		this.staffNumber = staffNumber;
	}
 */
	
	public Registration(Long recordNo, String firstName, String lastName, String phoneNumber,
			String alternativePhone, String emailAddress, String latitude, String longitude, String idNumber, LocalDateTime recordDatetime, Integer isStaff) {
		super();
		this.recordNo = recordNo;
		this.firstName = firstName;
		this.recordDatetime = recordDatetime;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.alternativePhone = alternativePhone;
		this.emailAddress = emailAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.idNumber = idNumber;
		this.isStaff = isStaff;
	}
	
	public Registration(Long recordNo, String firstName, String lastName, String phoneNumber, String alternativePhone,
			String emailAddress, String latitude, String longitude, String idNumber, Integer isStaff,
			String staffNumber, String homeAddress, LocalDateTime recordDatetime) {
		super();
		this.recordNo = recordNo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.alternativePhone = alternativePhone;
		this.emailAddress = emailAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.idNumber = idNumber;
		this.isStaff = isStaff;
		this.staffNumber = staffNumber;
		this.homeAddress = homeAddress;
		this.recordDatetime = recordDatetime;
	}
	public Registration(Long recordNo,  String firstName, String lastName, String phoneNumber, String latitude, String longitude, LocalDateTime recordDatetime, Integer isStaff) {
		super();
		this.recordNo = recordNo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.latitude = latitude;
		this.longitude = longitude;
		this.recordDatetime = recordDatetime;
		this.isStaff = isStaff;
	}
	
	public Registration(Long recordNo, String firstName, String lastName, String phoneNumber, String alternativePhone,
			String emailAddress, String latitude, String longitude, LocalDateTime recordDatetime, Integer isStaff) {
		this.recordNo = recordNo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.alternativePhone = alternativePhone;
		this.emailAddress = emailAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.recordDatetime = recordDatetime;
		this.isStaff = isStaff;
	}
	@Override
	public String toString() {
		return "Registration [recordNo=" + recordNo + ", recordDatetime=" + recordDatetime + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", alternativePhone=" + alternativePhone
				+ ", emailAddress=" + emailAddress + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", idNumber=" + idNumber + ", isStaff=" + isStaff + ", staffNumber=" + staffNumber + ", homeAddress="
				+ homeAddress + "]";
	}
	
}
