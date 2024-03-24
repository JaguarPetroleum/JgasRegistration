package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_Whitelist")
public class Whitelist {
	@Id
	String phoneNumber;

	public Whitelist(String phoneNumber) {
		super();
		this.phoneNumber = phoneNumber;
	}
	
	public Whitelist() {
		
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Whitelist [phoneNumber=" + phoneNumber + "]";
	}
}
