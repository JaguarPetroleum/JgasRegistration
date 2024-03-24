package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_OrderLN")
public class OrderLN {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer recordNo;
	Double buyingPrice;
	Double orderPrice;
	Integer orderQuantity;
	String orderNo;
	Double unitPrice;
	String productId;
	String refilledCylinderSerial;
	String weight;
	String serialNo;
	String productName;
	Integer discounted;
	Double discountAmount;
	public Integer getDiscounted() {
		return discounted;
	}
	public void setDiscounted(Integer discounted) {
		this.discounted = discounted;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(Integer recordNo) {
		this.recordNo = recordNo;
	}
	public Double getBuyingPrice() {
		return buyingPrice;
	}
	public void setBuyingPrice(Double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}
	public Double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getRefilledCylinderSerial() {
		return refilledCylinderSerial;
	}
	public void setRefilledCylinderSerial(String refilledCylinderSerial) {
		this.refilledCylinderSerial = refilledCylinderSerial;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public OrderLN() {
		
	}
	public OrderLN(Integer recordNo, Double buyingPrice, Double orderPrice, Integer orderQuantity, String orderNo,
			Double unitPrice, String productId, String refilledCylinderSerial, String weight, String serialNo,
			String productName) {
		super();
		this.recordNo = recordNo;
		this.buyingPrice = buyingPrice;
		this.orderPrice = orderPrice;
		this.orderQuantity = orderQuantity;
		this.orderNo = orderNo;
		this.unitPrice = unitPrice;
		this.productId = productId;
		this.refilledCylinderSerial = refilledCylinderSerial;
		this.weight = weight;
		this.serialNo = serialNo;
		this.productName = productName;
	}
	@Override
	public String toString() {
		return "OrderLN [recordNo=" + recordNo + ", buyingPrice=" + buyingPrice + ", orderPrice=" + orderPrice
				+ ", orderQuantity=" + orderQuantity + ", orderNo=" + orderNo + ", unitPrice=" + unitPrice
				+ ", productId=" + productId + ", refilledCylinderSerial=" + refilledCylinderSerial + ", weight="
				+ weight + ", serialNo=" + serialNo + ", productName=" + productName + ", discounted=" + discounted
				+ ", discountAmount=" + discountAmount + "]";
	}
	
}
