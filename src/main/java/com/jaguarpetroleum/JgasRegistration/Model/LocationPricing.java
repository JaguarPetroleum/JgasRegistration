package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_LocationPricing")
public class LocationPricing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer recordNumber;
	Double price;
	String productId;
	String locationId;
	public int getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public LocationPricing() {
		
	}
	public LocationPricing(int recordNumber, Double price, String productId, String locationId) {
		super();
		this.recordNumber = recordNumber;
		this.price = price;
		this.productId = productId;
		this.locationId = locationId;
	}
	@Override
	public String toString() {
		return "LocationPricing [recordNumber=" + recordNumber + ", price=" + price + ", productId=" + productId
				+ ", locationId=" + locationId + "]";
	}
	
}
