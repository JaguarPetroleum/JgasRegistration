package com.jaguarpetroleum.JgasRegistration.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class jmobilityIntegration {
	private static final Logger logger = LoggerFactory.getLogger(jmobilityIntegration.class);
	RestTemplate restTemplate = new RestTemplate();
	
	private String regionalId(JSONObject tripDetails) throws ParseException {
		int pickupCommaIndex = tripDetails.get("pickUpLatLng").toString().indexOf(",");
		int dropCommaIndex = tripDetails.get("dropOffLatLng").toString().indexOf(",");
		String payload = "{\r\n" + 
				"    \"start\": {\r\n" + 
				"        \"lat\": "+tripDetails.get("pickUpLatLng").toString().substring(0, pickupCommaIndex)+",\r\n" + 
				"        \"lng\": "+tripDetails.get("pickUpLatLng").toString().substring(pickupCommaIndex + 1)+"\r\n" + 
				"    },\r\n" + 
				"    \"end\": {\r\n" + 
				"        \"lat\": "+tripDetails.get("dropOffLatLng").toString().substring(0, dropCommaIndex)+",\r\n" + 
				"        \"lng\": "+tripDetails.get("dropOffLatLng").toString().substring(dropCommaIndex + 1)+"\r\n" + 
				"    }\r\n" + 
				"}";
		
		logger.info("Find the regional name payload "+payload);
		
		String url = "89.38.97.47:5001/v1/trip/tripinfo";		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<String>(payload,headers);
		String answer = restTemplate.postForObject(url, entity, String.class);
		
		JSONParser parser = new JSONParser();  
		JSONObject response = new JSONObject();
		
		try {
			 response = (JSONObject) parser.parse(answer);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		response = (JSONObject) parser.parse(response.get("data").toString());
	
		return response.getAsString("startPointRegionName");
	}
	
	private JSONObject bookRide(JSONObject bookDetails, String requestor) throws ParseException {
		bookDetails.appendField("regionalId", regionalId(bookDetails));
		bookDetails.appendField("requestor", requestor);
		bookDetails.appendField("type_of_payment", "Credit");
		bookDetails.appendField("trip_cost", "");
		
		logger.info("Received request to make a J-Mobility booking  "+bookDetails);
		
		String url = "89.38.97.47:5001/v1/customer/request/";		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<String>(bookDetails.toJSONString(),headers);
		String answer = restTemplate.postForObject(url, entity, String.class);
		
		JSONParser parser = new JSONParser();  
		JSONObject response = new JSONObject();
		
		try {
			 response = (JSONObject) parser.parse(answer);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return response ;
	}
}
