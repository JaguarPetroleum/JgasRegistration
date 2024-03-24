package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_User")
public class User {
	@Id
	String phoneNumber;
	String passcode;
	String role;
	int status;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPasscode() {
		return passcode;
	}
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User() {
		
	}
	public User(String phoneNumber, String passcode, String role, int status) {
		super();
		this.phoneNumber = phoneNumber;
		this.passcode = passcode;
		this.role = role;
		this.status = status;
	}
	@Override
	public String toString() {
		return "User [phoneNumber=" + phoneNumber + ", passcode=" + passcode + ", role=" + role + ", status=" + status
				+ "]";
	}
	
}
