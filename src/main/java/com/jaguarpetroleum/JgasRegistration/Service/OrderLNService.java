package com.jaguarpetroleum.JgasRegistration.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.OrderLN;
import com.jaguarpetroleum.JgasRegistration.Repository.OrderLNRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderLNService {
	@Autowired
	private OrderLNRepository orderLNRepository;
	
	public void save(OrderLN orderLN) {
		orderLNRepository.save(orderLN);
	}

	public List<OrderLN> findByOrderNo(String orderNo) {
		return orderLNRepository.findByOrderNo(orderNo);
	}
	
	public Integer updateScanDetails(String refilledCylinderSerial, String weight, String serialNo, String orderNo, String productId) {
		return orderLNRepository.updateScanDetails(refilledCylinderSerial, weight, serialNo, orderNo, productId);
	}
}
