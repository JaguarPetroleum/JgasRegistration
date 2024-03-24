package com.jaguarpetroleum.JgasRegistration.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.StaffDiscount;
import com.jaguarpetroleum.JgasRegistration.Repository.StaffDiscountRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StaffDiscountService {
	@Autowired
	private StaffDiscountRepository staffDiscountRepository;
	
	public void save(StaffDiscount staffDiscount) {
		staffDiscountRepository.save(staffDiscount);
	}
	
	public StaffDiscount findActiveDiscount() {
		return staffDiscountRepository.findActiveDiscount() ;
	}	
	
	public void updateActive() {
		staffDiscountRepository.updateActive(); 
	}	
}
