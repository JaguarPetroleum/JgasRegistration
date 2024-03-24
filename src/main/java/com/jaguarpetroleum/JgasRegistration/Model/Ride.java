package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_Ride")
public class Ride {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer recordNo;
	String orderNo;
	String driverName;
	String driverMobile;
	String color;
	String model;
	String plate;
	String startCode;
	String endCode;
	String tripId;
	String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	public String getStartCode() {
		return startCode;
	}
	public void setStartCode(String startCode) {
		this.startCode = startCode;
	}
	public String getEndCode() {
		return endCode;
	}
	public void setEndCode(String endCode) {
		this.endCode = endCode;
	}
	public Integer getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(Integer recordNo) {
		this.recordNo = recordNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverMobile() {
		return driverMobile;
	}
	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public Ride() {
		
	}
	
	public Ride(Integer recordNo, String orderNo, String driverName, String driverMobile, String color, String model,
			String plate, String startCode, String endCode, String tripId, String status) {
		super();
		this.recordNo = recordNo;
		this.orderNo = orderNo;
		this.driverName = driverName;
		this.driverMobile = driverMobile;
		this.color = color;
		this.model = model;
		this.plate = plate;
		this.startCode = startCode;
		this.endCode = endCode;
		this.tripId = tripId;
		this.status = status;
	}
	@Override
	public String toString() {
		return "Ride [recordNo=" + recordNo + ", orderNo=" + orderNo + ", driverName=" + driverName + ", driverMobile="
				+ driverMobile + ", color=" + color + ", model=" + model + ", plate=" + plate + ", startCode="
				+ startCode + ", endCode=" + endCode + ", tripId=" + tripId + ", status=" + status + "]";
	}
	
}
