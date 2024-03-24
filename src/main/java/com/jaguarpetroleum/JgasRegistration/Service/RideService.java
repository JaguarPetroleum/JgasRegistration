package com.jaguarpetroleum.JgasRegistration.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.Ride;
import com.jaguarpetroleum.JgasRegistration.Repository.RideRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RideService {

	@Autowired
	private RideRepository rideRepository;
	
	public Ride findByOrderNo(String orderNo) {
		return rideRepository.findByOrderNo(orderNo);
	}	
	
	public Ride findByTripId(String tripId) {
		return rideRepository.findByTripId(tripId);
	}	
	
	public void save(Ride ride) {
		rideRepository.save(ride);
	}
	
	public void updateCode(String startCode, String endCode, String tripId) {
		rideRepository.updateCodes(startCode, endCode, tripId);		
	}
	
	public void updateStatus(String status, String tripId) {
		rideRepository.updateStatus(status, tripId);		
	}
}
