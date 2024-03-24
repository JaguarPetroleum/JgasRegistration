package com.jaguarpetroleum.JgasRegistration.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jaguarpetroleum.JgasRegistration.Model.LocationPricing;
import com.jaguarpetroleum.JgasRegistration.Repository.LocationPricingRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationPricingService {
	@Autowired
	private LocationPricingRepository locationPricingRepository;
	
	public LocationPricing findByProductIdandLocationId(String productId, String locationId) {
		return locationPricingRepository.findByProductIdandLocationId(productId, locationId);
	}
	
	public List<LocationPricing> findByLocationIdforStaff(String locationId, Double staffDiscount) {
		return locationPricingRepository.findByLocationIdforStaff(locationId, staffDiscount);
	}
	
	public void save(LocationPricing locationPricing) {
		locationPricingRepository.save(locationPricing);
	}

	public List<LocationPricing> findByLocationId(String locationId) {
		return locationPricingRepository.findByLocationId(locationId);
	}
	
	public void updatePricing(Double price, String locationId, String productId) {
		locationPricingRepository.updatePricing(price, locationId, productId);
	}

}
