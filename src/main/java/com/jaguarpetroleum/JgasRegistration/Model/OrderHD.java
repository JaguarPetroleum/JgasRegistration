package com.jaguarpetroleum.JgasRegistration.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_OrderHD")
public class OrderHD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer recordNo;
	String returnCylinder;
	Double totalCost;
	Double totalProductCost;
	String phoneNumber;
	String locationId;
	LocalDateTime orderDatetime;
	String orderNo;
	String paymentMethod;
	String specificLocation;
	String status;
	String transactionRef;
	Integer delivery;
	double deliveryCost;
	Double destinationLatitude;
	Double destinationLongitude;
	Double distance;
	Double pickUpLatitude;
	Double pickUpLongitude;
	Integer paid;
	String checkOutRequestId;
	String mpesaDescription;
	String recepientName;
	String recepientPhone;
	String customerName;
	String customerNumber;
	String deliveryCode;
	String customerLocationName;
	public String getCustomerLocationName() {
		return customerLocationName;
	}
	public void setCustomerLocationName(String customerLocationName) {
		this.customerLocationName = customerLocationName;
	}
	public String getDeliveryCode() {
		return deliveryCode;
	}
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getRecepientName() {
		return recepientName;
	}
	public void setRecepientName(String recepientName) {
		this.recepientName = recepientName;
	}
	public String getRecepientPhone() {
		return recepientPhone;
	}
	public void setRecepientPhone(String recepientPhone) {
		this.recepientPhone = recepientPhone;
	}
	public String getMpesaDescription() {
		return mpesaDescription;
	}
	public void setMpesaDescription(String mpesaDescription) {
		this.mpesaDescription = mpesaDescription;
	}
	public String getCheckOutRequestId() {
		return checkOutRequestId;
	}
	public void setCheckOutRequestId(String checkOutRequestId) {
		this.checkOutRequestId = checkOutRequestId;
	}
	public Integer getPaid() {
		return paid;
	}
	public void setPaid(Integer paid) {
		this.paid = paid;
	}
	public Integer getDelivery() {
		return delivery;
	}
	public void setDelivery(Integer delivery) {
		this.delivery = delivery;
	}
	public double getDeliveryCost() {
		return deliveryCost;
	}
	public void setDeliveryCost(double d) {
		this.deliveryCost = d;
	}
	public Double getDestinationLatitude() {
		return destinationLatitude;
	}
	public void setDestinationLatitude(Double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}
	public Double getDestinationLongitude() {
		return destinationLongitude;
	}
	public void setDestinationLongitude(Double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Double getPickUpLatitude() {
		return pickUpLatitude;
	}
	public void setPickUpLatitude(Double pickUpLatitude) {
		this.pickUpLatitude = pickUpLatitude;
	}
	public Double getPickUpLongitude() {
		return pickUpLongitude;
	}
	public void setPickUpLongitude(Double pickUpLongitude) {
		this.pickUpLongitude = pickUpLongitude;
	}
	public Integer getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(Integer recordNo) {
		this.recordNo = recordNo;
	}
	public String getReturnCylinder() {
		return returnCylinder;
	}
	public void setReturnCylinder(String string) {
		this.returnCylinder = string;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Double getTotalProductCost() {
		return totalProductCost;
	}
	public void setTotalProductCost(Double totalProductCost) {
		this.totalProductCost = totalProductCost;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public LocalDateTime getOrderDatetime() {
		return orderDatetime;
	}
	public void setOrderDatetime(LocalDateTime localDateTime) {
		this.orderDatetime = localDateTime;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getSpecificLocation() {
		return specificLocation;
	}
	public void setSpecificLocation(String specificLocation) {
		this.specificLocation = specificLocation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactionRef() {
		return transactionRef;
	}
	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}
	public OrderHD(Integer recordNo, String returnCylinder, Double totalCost, Double totalProductCost,
			String phoneNumber, String locationId, LocalDateTime orderDatetime, String orderNo, String paymentMethod,
			String specificLocation, String status, String transactionRef, Integer delivery, double deliveryCost,
			Double destinationLatitude, Double destinationLongitude, Double distance, Double pickUpLatitude,
			Double pickUpLongitude, Integer paid, String checkOutRequestId, String mpesaDescription,
			String recepientName, String recepientPhone, String customerName, String customerNumber, String customerLocationName) {
		super();
		this.recordNo = recordNo;
		this.returnCylinder = returnCylinder;
		this.totalCost = totalCost;
		this.totalProductCost = totalProductCost;
		this.phoneNumber = phoneNumber;
		this.locationId = locationId;
		this.orderDatetime = orderDatetime;
		this.orderNo = orderNo;
		this.paymentMethod = paymentMethod;
		this.specificLocation = specificLocation;
		this.status = status;
		this.transactionRef = transactionRef;
		this.delivery = delivery;
		this.deliveryCost = deliveryCost;
		this.destinationLatitude = destinationLatitude;
		this.destinationLongitude = destinationLongitude;
		this.distance = distance;
		this.pickUpLatitude = pickUpLatitude;
		this.pickUpLongitude = pickUpLongitude;
		this.paid = paid;
		this.checkOutRequestId = checkOutRequestId;
		this.mpesaDescription = mpesaDescription;
		this.recepientName = recepientName;
		this.recepientPhone = recepientPhone;
		this.customerName = customerName;
		this.customerNumber = customerNumber;
		this.customerLocationName = customerLocationName;
	}
	public OrderHD(Integer recordNo, String returnCylinder, Double totalCost, Double totalProductCost,
			String phoneNumber, String locationId, LocalDateTime orderDatetime, String orderNo, String paymentMethod,
			String specificLocation, String status, String transactionRef, Integer delivery, double deliveryCost,
			Double destinationLatitude, Double destinationLongitude, Double distance, Double pickUpLatitude,
			Double pickUpLongitude, Integer paid, String checkOutRequestId, String mpesaDescription,
			String recepientName, String recepientPhone, String customerLocationName) {
		super();
		this.recordNo = recordNo;
		this.returnCylinder = returnCylinder;
		this.totalCost = totalCost;
		this.totalProductCost = totalProductCost;
		this.phoneNumber = phoneNumber;
		this.locationId = locationId;
		this.orderDatetime = orderDatetime;
		this.orderNo = orderNo;
		this.paymentMethod = paymentMethod;
		this.specificLocation = specificLocation;
		this.status = status;
		this.transactionRef = transactionRef;
		this.delivery = delivery;
		this.deliveryCost = deliveryCost;
		this.destinationLatitude = destinationLatitude;
		this.destinationLongitude = destinationLongitude;
		this.distance = distance;
		this.pickUpLatitude = pickUpLatitude;
		this.pickUpLongitude = pickUpLongitude;
		this.paid = paid;
		this.checkOutRequestId = checkOutRequestId;
		this.mpesaDescription = mpesaDescription;
		this.recepientName = recepientName;
		this.recepientPhone = recepientPhone;
		this.customerLocationName = customerLocationName;
	}
	public OrderHD() {
		
	}
	@Override
	public String toString() {
		return "OrderHD [recordNo=" + recordNo + ", returnCylinder=" + returnCylinder + ", totalCost=" + totalCost
				+ ", totalProductCost=" + totalProductCost + ", phoneNumber=" + phoneNumber + ", locationId="
				+ locationId + ", orderDatetime=" + orderDatetime + ", orderNo=" + orderNo + ", paymentMethod="
				+ paymentMethod + ", specificLocation=" + specificLocation + ", status=" + status + ", transactionRef="
				+ transactionRef + ", delivery=" + delivery + ", deliveryCost=" + deliveryCost
				+ ", destinationLatitude=" + destinationLatitude + ", destinationLongitude=" + destinationLongitude
				+ ", distance=" + distance + ", pickUpLatitude=" + pickUpLatitude + ", pickUpLongitude="
				+ pickUpLongitude + ", paid=" + paid + ", checkOutRequestId=" + checkOutRequestId
				+ ", mpesaDescription=" + mpesaDescription + ", recepientName=" + recepientName + ", recepientPhone="
				+ recepientPhone + ", customerName=" + customerName + ", customerNumber=" + customerNumber
				+ ", deliveryCode=" + deliveryCode + ", customerLocationName=" + customerLocationName + "]";
	}
		
}
