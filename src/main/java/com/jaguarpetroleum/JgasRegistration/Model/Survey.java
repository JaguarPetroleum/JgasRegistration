package com.jaguarpetroleum.JgasRegistration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_Survey")
public class Survey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer rateId;
	String orderNo;
	Integer rate;
	String phoneNumber;
	String communicationProcess;
	String orderProcess;
	String paymentProcess;
	String response;
	String additionalComments;
	public String getAdditionalComments() {
		return additionalComments;
	}
	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}
	public Integer getRateId() {
		return rateId;
	}
	public void setRateId(Integer rateId) {
		this.rateId = rateId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCommunicationProcess() {
		return communicationProcess;
	}
	public void setCommunicationProcess(String communicationProcess) {
		this.communicationProcess = communicationProcess;
	}
	public String getOrderProcess() {
		return orderProcess;
	}
	public void setOrderProcess(String orderProcess) {
		this.orderProcess = orderProcess;
	}
	public String getPaymentProcess() {
		return paymentProcess;
	}
	public void setPaymentProcess(String paymentProcess) {
		this.paymentProcess = paymentProcess;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Survey() {
		
	}
	public Survey(Integer rateId, String orderNo, Integer rate, String phoneNumber, String communicationProcess,
			String orderProcess, String paymentProcess, String response, String additionalComments) {
		super();
		this.rateId = rateId;
		this.orderNo = orderNo;
		this.rate = rate;
		this.phoneNumber = phoneNumber;
		this.communicationProcess = communicationProcess;
		this.orderProcess = orderProcess;
		this.paymentProcess = paymentProcess;
		this.response = response;
		this.additionalComments = additionalComments;
	}
	@Override
	public String toString() {
		return "Survey [rateId=" + rateId + ", orderNo=" + orderNo + ", rate=" + rate + ", phoneNumber=" + phoneNumber
				+ ", communicationProcess=" + communicationProcess + ", orderProcess=" + orderProcess
				+ ", paymentProcess=" + paymentProcess + ", response=" + response + ", additionalComments="
				+ additionalComments + "]";
	}
	
}
