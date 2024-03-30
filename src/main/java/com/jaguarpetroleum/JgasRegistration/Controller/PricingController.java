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
import com.jaguarpetroleum.JgasRegistration.Model.LocationPricing;
import com.jaguarpetroleum.JgasRegistration.Model.Product;
import com.jaguarpetroleum.JgasRegistration.Model.Registration;
import com.jaguarpetroleum.JgasRegistration.Model.StaffDiscount;
import com.jaguarpetroleum.JgasRegistration.Service.LittleCabTariffService;
import com.jaguarpetroleum.JgasRegistration.Service.LocationPricingService;
import com.jaguarpetroleum.JgasRegistration.Service.LocationService;
import com.jaguarpetroleum.JgasRegistration.Service.ProductService;
import com.jaguarpetroleum.JgasRegistration.Service.RegistrationService;
import com.jaguarpetroleum.JgasRegistration.Service.StaffDiscountService;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class PricingController {
	private static final Logger logger = LoggerFactory.getLogger(PricingController.class);
	
	@Autowired
	private LocationService locationService;	
	@Autowired
	private LocationPricingService locationPricingService;	
	@Autowired
	private ProductService productService;	
	@Autowired
	private LittleCabTariffService littleCabTariffService;	
	@Autowired
	private RegistrationService registrationService;
	@Autowired
	private StaffDiscountService staffDiscountService;
	
	@GetMapping("/getByLocationPricing/{locationName}/{phoneNumber}")
	public JSONArray get(@PathVariable String locationName, @PathVariable String phoneNumber){
		JSONArray response = new JSONArray();
		Registration myRegistration = registrationService.findByPhoneNumber(phoneNumber);
		/*
		 * Get locationId from location name 
		 * Get  Products from location and the Pricing
		 * aPPend details from Products table
		 */
		Location location = locationService.findByLocationName(locationName);
		if(location != null) {
			logger.info("Details to location "+locationName +" found");
			List<LocationPricing> locationPrices;
			
			if(myRegistration.getIsStaff() != null && myRegistration.getIsStaff()==1) {
				logger.info("User "+phoneNumber +" is tagged as a staff. Finding staff offer attached to Gas products");
				//Get the discount amount
				StaffDiscount staffDiscount = staffDiscountService.findActiveDiscount(locationName);
				Double staffOffer = 0.00;
				if(staffDiscount != null) {
					staffOffer = staffDiscount.getDiscountAmount();
					logger.info("Staff offer of KES "+staffOffer+" to be applied to customer");
				}
				locationPrices = locationPricingService.findByLocationIdforStaff(location.getLocationId(), staffOffer);
			} else {
				locationPrices = locationPricingService.findByLocationId(location.getLocationId());
			}
			
			if(locationPrices != null) {
				for (int i = 0; i < locationPrices.size(); i++) {
					JSONObject newItem = new JSONObject();
					
		            LocationPricing item = locationPrices.get(i);
		            Product product = productService.findByProductId(item.getProductId());
		            
		            newItem.put("locationId", item.getLocationId());
		            newItem.put("productPrice", item.getPrice());            
		            newItem.put("productDescription", product.getProductDescription());   
		            newItem.put("productId", product.getProductId());
		            newItem.put("productImagePath", product.getProductImagePath());
		            newItem.put("productName", product.getProductName());
		            
		            response.add(newItem);
		        }
				logger.info("Location product details successfully generated "+response);
			} else {
				logger.warn("Location "+locationName +" prices not yet captured");
			}
		} else {
			logger.warn("Details to location "+locationName +" not found");
		}
		return response;
	}
	
	@GetMapping("/transportCharge/{distance}")
	public JSONObject getTransportCharge(@PathVariable String distance){
		JSONObject response = new JSONObject();
		try {
			return littleCabTariffService.findByDistance(distance);
		} catch(Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", e.getLocalizedMessage());
		}
		return response;
	}
	
	@GetMapping("/defaultLocations/{phoneNumber}/{locationId}")
	public JSONObject getDefaultLocations(@PathVariable String phoneNumber, @PathVariable String locationId){
		logger.info("Get geocordinates for number "+phoneNumber);
		JSONObject response = new JSONObject();
		try {
			Registration register = new Registration();
			register = registrationService.findByPhoneNumber(phoneNumber);
			Location location = new Location();
			location = locationService.findByLocationId(locationId.toUpperCase());
			response.put("pickLatitude", location.getLatitude());
			response.put("pickLongitude", location.getLongitude());
			response.put("dropLatitude", register.getLatitude());
			response.put("dropLongitude", register.getLongitude());
			response.put("homeAddress", register.getHomeAddress());
			
			response.put("resultCode", 0);
			response.put("resultMessage", "Successfully Retrieved locations Geo-Cordinates");
			
		} catch(Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", e.getLocalizedMessage());
		}
		logger.info("Response for finding geo-cordinates "+response);
		return response;
	}

	@PostMapping("/createPrice")
	public JSONObject createPrice(@RequestBody JSONObject locationPricing) {
		logger.info("Location Price creation request received "+locationPricing);
		JSONObject response = new JSONObject();
		
		if(locationService.findByLocationId(locationPricing.get("locationId").toString()) != null) {
			if(productService.findByProductId(locationPricing.get("productId").toString()) != null) {
				logger.info("Location Price validation begins ");
				LocationPricing myLocationPricing = new LocationPricing();
				LocationPricing myLocationPricing2 = new LocationPricing();
				myLocationPricing = locationPricingService.findByProductIdandLocationId(locationPricing.get("productId").toString() , locationPricing.get("locationId").toString());
				try {		
					
					if(myLocationPricing == null) {
						logger.info("Product pricing not found in location . New entry to be created");
						myLocationPricing2.setLocationId(locationPricing.get("locationId").toString());
						myLocationPricing2.setProductId(locationPricing.get("productId").toString());
						myLocationPricing2.setPrice(Double.parseDouble(locationPricing.get("price").toString()));
						
						locationPricingService.save(myLocationPricing2);
						response.put("resultCode", 0);
						response.put("resultMessage", "Product "+locationPricing.get("productId").toString() + " price at location "+locationPricing.get("locationId").toString() +" has been set successfully");
						
					} else {
						response.put("resultCode", 0);
						response.put("resultMessage", "Product "+locationPricing.get("productId").toString() + " price at location "+locationPricing.get("locationId").toString() +" available. You can update the record");
					}			
				} catch (Exception e) {
					response.put("resultCode", 20010);
					response.put("resultMessage", "An error occured while saving location pricing details "+e.getMessage());
				}
			} else {
				response.put("resultCode", 40010);
				response.put("resultMessage", "There is no product identified by productId  "+locationPricing.get("productId").toString());
			}
			
		} else {
			response.put("resultCode", 30010);
			response.put("resultMessage", "There is no location identified by locationId  "+locationPricing.get("locationId").toString());
		}
				
		logger.info("Location Price creation response "+response);
		return response;
	}
	
	@PutMapping("/updatePrice")
	public JSONObject updatePrice(@RequestBody JSONObject locationPricing) {
		logger.info("Location Price udate request received "+locationPricing);
		JSONObject response = new JSONObject();
		
		if(locationService.findByLocationId(locationPricing.get("locationId").toString()) != null) {
			if(productService.findByProductId(locationPricing.get("productId").toString()) != null) {
				logger.info("Location Price validation begins ");
				LocationPricing myLocationPricing = new LocationPricing();
				LocationPricing myLocationPricing2 = new LocationPricing();
				myLocationPricing = locationPricingService.findByProductIdandLocationId(locationPricing.get("productId").toString() , locationPricing.get("locationId").toString());
				try {		
					
					if(myLocationPricing == null) {						
						response.put("resultCode", 0);
						response.put("resultMessage", "Product "+locationPricing.get("productId").toString() + " and location "+locationPricing.get("locationId").toString() +" combination not found. Update cannot be done");
						
					} else {
						logger.info("product pricing found in location . Update to be done");
						myLocationPricing2.setLocationId(locationPricing.get("locationId").toString());
						myLocationPricing2.setProductId(locationPricing.get("productId").toString());
						myLocationPricing2.setPrice(Double.parseDouble(locationPricing.get("price").toString()));
						
						locationPricingService.updatePricing(Double.parseDouble(locationPricing.get("price").toString()), 
								locationPricing.get("locationId").toString(), 
								locationPricing.get("productId").toString());
						response.put("resultCode", 0);
						response.put("resultMessage", "Product "+locationPricing.get("productId").toString() + " price at location "+locationPricing.get("locationId").toString() +" has been successfully updated");
					}			
				} catch (Exception e) {
					response.put("resultCode", 20010);
					response.put("resultMessage", "An error occured while updating location pricing details "+e.getMessage());
				}
			} else {
				response.put("resultCode", 40010);
				response.put("resultMessage", "There is no product identified by productId  "+locationPricing.get("productId").toString());
			}
			
		} else {
			response.put("resultCode", 30010);
			response.put("resultMessage", "There is no location identified by locationId  "+locationPricing.get("locationId").toString());
		}
				
		logger.info("Location Price update response "+response);
		return response;
	}
	
}
