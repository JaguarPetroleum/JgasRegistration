package com.jaguarpetroleum.JgasRegistration.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jaguarpetroleum.JgasRegistration.Model.Location;
import com.jaguarpetroleum.JgasRegistration.Service.LocationService;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class LocationController {
	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	@Autowired
	private LocationService locationService;
	
	@PostMapping("/createLocation")
	public JSONObject add(@RequestBody JSONObject locationDetails) {
		logger.info("Received location creation details "+locationDetails);
		JSONObject response = new JSONObject();
		
		Location location = new Location();
		
		try {
			location.setLatitude(locationDetails.get("latitude").toString()); 
			location.setLocationDescrition(locationDetails.get("locationDescription").toString());
			location.setLocationId(locationDetails.get("locationId").toString());
			location.setLocationName(locationDetails.get("locationName").toString());
			location.setLongitude(locationDetails.get("longitude").toString());
			location.setPhoneNumber(locationDetails.get("phoneNumber").toString());
			location.setStatus(locationDetails.get("status").toString());
			
			locationService.save(location);
			logger.info("New location created. Location name "+locationDetails.get("locationName").toString());
			
			response.put("resultCode", 0);
			response.put("resultMessage", "Location "+locationDetails.get("locationName").toString()+" has been successfully created");
		} catch (Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "An error occured while creating location. Details: "+e.getMessage());
		}
		
		logger.info("Location creation response "+response);		
		return response;
	}
	
	@PutMapping("/updateLocation")
	public JSONObject updateLocation(@RequestBody JSONObject locationDetails) {
		logger.info("Received location details to update "+locationDetails);
		JSONObject response = new JSONObject();
		
		Location location = new Location();
		
		try {
			if(locationService.findByLocationId(locationDetails.get("locationId").toString()) != null) {
				location.setLatitude(locationDetails.get("latitude").toString()); 
				location.setLocationDescrition(locationDetails.get("locationDescription").toString());
				location.setLocationId(locationDetails.get("locationId").toString());
				location.setLocationName(locationDetails.get("locationName").toString());
				location.setLongitude(locationDetails.get("longitude").toString());
				location.setPhoneNumber(locationDetails.get("phoneNumber").toString());
				location.setStatus(locationDetails.get("status").toString());
				
				locationService.save(location);
				logger.info("Location "+locationDetails.get("locationName").toString()+" updated successfully");
				
				response.put("resultCode", 0);
				response.put("resultMessage", "Location "+locationDetails.get("locationName").toString()+" has been successfully updated");
			} else {
				response.put("resultCode", 20010);
				response.put("resultMessage", "There is no location identified by locationId "+locationDetails.get("locationId").toString());
			}
			
		} catch (Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "An error occured while updating location. "+e.getMessage());
		}
		
		logger.info("Location update response "+response);		
		return response;
	}
	
	@GetMapping("/allLocations")
	public List<Location> getAllLocations(){
		try {
			return locationService.listAll();

		} catch(Exception e) {
			return null;
		}		
	}
	
	@GetMapping("/locationByName/{locationName}")
	public Location locationByName(@PathVariable String locationName){
		logger.info("Received request for Location details "+ locationName);		
		try {
			return locationService.findByLocationName(locationName);
		} catch(Exception e) {
			logger.error("Error encountered while retrieving location details "+e.getMessage());
			return null;
		}				
	}
	
	@GetMapping("/locationById/{locationId}")
	public Location locationById(@PathVariable String locationId){
		logger.info("Received request for Location details "+ locationId);		
		try {
			return locationService.findByLocationId(locationId);
		} catch(Exception e) {
			logger.error("Error encountered while retrieving location details "+e.getMessage());
			return null;
		}				
	}
	
	@PostMapping("/notifications")
	public JSONObject notifications(@RequestBody JSONObject request) {
		JSONObject response = new JSONObject();
		logger.info("Received new notification token details "+request);
		
		response.put("resultCode", 0);
		response.put("resultMessage", "Success");
		return response;
	}
}
