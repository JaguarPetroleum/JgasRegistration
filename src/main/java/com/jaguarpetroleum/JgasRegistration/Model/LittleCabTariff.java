package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_LittleCabTariff")
public class LittleCabTariff {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer recordNumber;
	Integer floor;
	Integer ceiling;
	Double rate;
	public Integer getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Integer getCeiling() {
		return ceiling;
	}
	public void setCeiling(Integer ceiling) {
		this.ceiling = ceiling;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public LittleCabTariff() {
		
	}
	public LittleCabTariff(Integer recordNumber, Integer floor, Integer ceiling, Double rate) {
		super();
		this.recordNumber = recordNumber;
		this.floor = floor;
		this.ceiling = ceiling;
		this.rate = rate;
	}
	@Override
	public String toString() {
		return "LittleCabTariff [recordNumber=" + recordNumber + ", floor=" + floor + ", ceiling=" + ceiling + ", rate="
				+ rate + "]";
	}
	
}
