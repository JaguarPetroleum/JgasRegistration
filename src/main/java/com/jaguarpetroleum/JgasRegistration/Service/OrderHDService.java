package com.jaguarpetroleum.JgasRegistration.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.OrderHD;
import com.jaguarpetroleum.JgasRegistration.Repository.OrderHDRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@EnableScheduling
public class OrderHDService {

	@Autowired
	private OrderHDRepository orderHDRepository;
	
	public OrderHD findByOrderNo(String orderNo) {
		return orderHDRepository.findByOrderNo(orderNo);
	}
	
	public OrderHD findDeliveryCode(String deliveryCode) {
		return orderHDRepository.findByDeliveryCode(deliveryCode);
	}
	
	public void save(OrderHD orderHD) {
		orderHDRepository.save(orderHD);
	}

	public List<OrderHD> findByLocationId(String locationId) {
		return orderHDRepository.findByLocationId(locationId);
	}
	
	public List<OrderHD> findByLocationId2(String locationId) {
		return orderHDRepository.findByLocationId2(locationId);
	}
	
	public List<OrderHD> findByPhoneNumber(String phoneNumber) {
		return orderHDRepository.findByPhoneNumber(phoneNumber);
	}
	
	public void updateCheckout(String checkOutRequestId, String orderNo) {
		 orderHDRepository.updateCheckout(checkOutRequestId, orderNo);		
	}
	
	public void updateDeliveryCode(String deliveryCode, String orderNo) {
		 orderHDRepository.updateDeliveryCode(deliveryCode, orderNo);		
	}
	
	public void updateStatus(String status, String orderNo) {
		 orderHDRepository.updateStatus(status, orderNo);		
	}
	
	public void updateRecepient(String recepientName, String recepientPhone, String orderNo) {
		 orderHDRepository.updateRecepient(recepientName, recepientPhone, orderNo);		
	}
	
	public void updatePaymentDetails(String checkOutRequestId, String transactionRef, int resultCode, String resultDesc, String status) {
		 orderHDRepository.updatePaymentDetails(checkOutRequestId, transactionRef, resultCode, resultDesc, status);		
	}
	
	public void updatePaymentDetailsC2B(String orderNo, String transactionRef, int resultCode, String resultDesc, String status) {
		 orderHDRepository.updatePaymentDetailsC2B(orderNo, transactionRef, resultCode, resultDesc, status);		
	}
	
	
	public List<OrderHD> listAll(){
		return orderHDRepository.findAll();
	}

	public OrderHD findByOrderNoAndAmount(String orderNo, String amount) {
		return orderHDRepository.findByOrderNoAndAmount(orderNo, Double.parseDouble(amount));
	}
	
	public OrderHD findByCheckOutId(String mpesaCheckOutId) {
		return orderHDRepository.findByCheckOutId(mpesaCheckOutId);
	}
	
	public Integer staffDiscountTimes(String phoneNumber) {
		if(orderHDRepository.staffDiscountTimes(phoneNumber)==null) {
			return 0;
		} else {
			return orderHDRepository.staffDiscountTimes(phoneNumber);
		}		
	}
	
	@Scheduled(cron = "0 */5 * * * *")
	public void cancelUnpaidOrders() {
		 orderHDRepository.cancelUnpaidOrders();		
	}
	

}
