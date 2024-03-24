package com.jaguarpetroleum.JgasRegistration.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
public class NetsuiteController {
	private static final Logger logger = LoggerFactory.getLogger(NetsuiteController.class);
	
	@GetMapping("/getLocationStockLevels/{locationId}")
	public JSONObject getLocationStockLevels(@PathVariable String locationId) {
		logger.info("Received stock level report request for location "+locationId);
		JSONObject response = new JSONObject();
		
		//Get the netsuite locationId
		return response;
	}
	
	@PostMapping("/replenishRequest")
	public JSONObject replenishRequest(@RequestBody JSONObject replenishRequest) {
		logger.info("Received replenishment request "+replenishRequest);
		JSONObject response = new JSONObject();
		//Have item and quantity
		return response;
	}
}
