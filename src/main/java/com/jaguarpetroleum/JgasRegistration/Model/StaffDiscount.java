package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_StaffOffer")
public class StaffDiscount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer recordNo;
	Double discountAmount;
	public Integer getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(Integer recordNo) {
		this.recordNo = recordNo;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public StaffDiscount() {
		
	}
	public StaffDiscount(Integer recordNo, Double discountAmount) {
		super();
		this.recordNo = recordNo;
		this.discountAmount = discountAmount;
	}
	@Override
	public String toString() {
		return "StaffDiscount [recordNo=" + recordNo + ", discountAmount=" + discountAmount + "]";
	}	

}
