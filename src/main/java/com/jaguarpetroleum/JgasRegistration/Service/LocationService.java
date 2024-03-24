package com.jaguarpetroleum.JgasRegistration.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.Location;
import com.jaguarpetroleum.JgasRegistration.Model.OrderHD;
import com.jaguarpetroleum.JgasRegistration.Repository.LocationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationService {
	@Autowired
	private LocationRepository locationRepository;
	
	public Location findByLocationId(String locationId) {
		return locationRepository.findByLocationId(locationId);
	}
	
	public Location findByPhoneNumber(String phoneNumber) {
		return locationRepository.findByPhoneNumber(phoneNumber);
	}
	
	public Location findByLocationName(String locationName) {
		return locationRepository.findByLocationName(locationName);
	}
	
	public void save(Location location) {
		locationRepository.save(location);
	}
	
	public List<Location> listAll(){
		return locationRepository.findAll();
	}
}
