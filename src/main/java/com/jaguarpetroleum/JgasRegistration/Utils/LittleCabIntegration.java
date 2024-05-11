package com.jaguarpetroleum.JgasRegistration.Utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jaguarpetroleum.JgasRegistration.Model.Location;
import com.jaguarpetroleum.JgasRegistration.Model.OrderHD;
import com.jaguarpetroleum.JgasRegistration.Model.Registration;
import com.jaguarpetroleum.JgasRegistration.Model.Ride;
import com.jaguarpetroleum.JgasRegistration.Service.LittleCabTariffService;
import com.jaguarpetroleum.JgasRegistration.Service.LocationService;
import com.jaguarpetroleum.JgasRegistration.Service.OrderHDService;
import com.jaguarpetroleum.JgasRegistration.Service.RegistrationService;
import com.jaguarpetroleum.JgasRegistration.Service.RideService;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
@RestController
public class LittleCabIntegration {
	private static final Logger logger = LoggerFactory.getLogger(LittleCabIntegration.class);
	
	@Autowired
	LittleCabTariffService littleCabTariffService;
	@Autowired
	OrderHDService orderHDService;
	@Autowired
	RegistrationService registrationService;
	@Autowired
	LocationService locationService;
	@Autowired
	RideService rideService;	
	@Autowired
	jmobilityIntegration jmobility;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
                .setConnectTimeout(Duration.ofMillis(120000))
                .setReadTimeout(Duration.ofMillis(120000))
                .build();
	}
	
	RestTemplate restTemplate = new RestTemplate();
	
	@GetMapping("/littleCabGenerateToken")
	public JSONObject generateToken() throws RestClientException, ParseException {
		String tokenizationEndpoint = com.jaguarpetroleum.JgasRegistration.Configs.Constants.LITTLETOKENIZATIONENDPOINT;
		JSONObject response = new JSONObject();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);			
		
		String plainCreds = com.jaguarpetroleum.JgasRegistration.Configs.Constants.LITTLEKEY+":"+com.jaguarpetroleum.JgasRegistration.Configs.Constants.LITTLESECRET;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		
		headers.add("Authorization", "Basic " + base64Creds);			
		
		HttpEntity <String> entity = new HttpEntity<String>(headers);
	      
		JSONParser parser = new JSONParser(); 
		response = (JSONObject) parser.parse(restTemplate.exchange(tokenizationEndpoint , HttpMethod.GET, entity, String.class).getBody());
	      
		return response;
	}
	
	@GetMapping("/estimateRide/{fromLatLong}/{toLatLong}")
	public JSONObject estimateRide(@PathVariable String fromLatLong, @PathVariable String toLatLong) throws RestClientException, ParseException {		
		String estimateEndpoint = "https://api.little.bz/service/ride/estimate?from_latlng="+fromLatLong+"&to_latlng="+toLatLong+"";
		logger.info("Request to estimate ride "+estimateEndpoint);
		
		JSONObject response = new JSONObject();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.setBearerAuth(generateToken().get("token").toString());
		HttpEntity <String> entity = new HttpEntity<String>(headers);
	      
		JSONParser parser = new JSONParser(); 
		response = (JSONObject) parser.parse(restTemplate.exchange(estimateEndpoint , HttpMethod.GET, entity, String.class).getBody());		
		
		JSONObject res = new JSONObject();
		res.put("servicePay", littleCabTariffService.findByDistance(response.get("distance").toString()).get("rate"));
		res.put("distance", response.get("distance").toString());
		res.put("time", response.get("time").toString());
		
		logger.info("Estimate ride details "+res);
		return res;
	}
	
	@PostMapping("/littleCabBookRide")
	public JSONObject bookRide(@RequestBody JSONObject details) throws RestClientException, ParseException, URISyntaxException {
		//Added code bloc to prevent multiple ride assignment

  		if(rideService.findByOrderNo(details.get("orderNo").toString()) != null) {
			logger.info("Order "+ details.get("orderNo").toString() +" already has an active rider assigned");
			return null;
		}	
  		
  		
  		try {
  			//Call for a jmobility rider. Remove the try block to return to littlecab as sole mobility partner
  			JSONObject resp = new JSONObject();
  			
  			JSONObject jmobResponse = new JSONObject();
  			jmobResponse = jmobility.bookRide(details);  		
  			
  			if(jmobResponse.get("message").toString().toUpperCase().equals("SUCCESS")) {
  				Map body = (Map) jmobResponse.get("data");
  	  			JSONObject dataBody = new JSONObject(body);	
  	  			Map rider = (Map) dataBody.get("rider");
  	  			
  	  			JSONObject driver = new JSONObject();
  	  			JSONObject driverDetails = new JSONObject(rider);
  	  			driver.put("name", driverDetails.get("name").toString());
  	  			driver.put("mobile",driverDetails.get("phone").toString());
  	  			driver.put("rating","");
  	  			driver.put("email","");
  				driver.put("picture","");
  	  			
  	  			JSONObject carDetails = new JSONObject();
  	  			carDetails.put("color","");
  	  			carDetails.put("distance","");
  	  			carDetails.put("model",driverDetails.get("vehicle_make").toString()+" "+driverDetails.get("vehicle_model").toString());
  	  			carDetails.put("plate",driverDetails.get("number_plate").toString());
  	  			carDetails.put("time","");
  	  			carDetails.put("latlng",driverDetails.get("latitude").toString()+","+ driverDetails.get("longitude").toString());
  	  			
  	  			
  	  			Map trip = (Map) driverDetails.get("tripCodes");
  	  			JSONObject tripCodes = new JSONObject(trip);	
  	  			resp.put("distance", "0.00");
  	  			resp.put("driver", driver);
  	  			resp.put("car", carDetails);
  	  			resp.put("tripId", tripCodes.get("tripId").toString());
  	  			resp.put("endCode", tripCodes.get("endCode").toString());
  				resp.put("startCode", tripCodes.get("startCode").toString());
  	  			resp.put("time", "0.00");
  	  			resp.appendField("orderNo", details.get("orderNo").toString());
  	  			resp.appendField("provider", "J-Mobility");
  	  			
  	  			logger.info("Converted J-Mobility response payload to parse "+resp);
  	  			
  	  			return parseBookRide(resp);
  	  			
  			}  else {
  				return null;
  			}
  			
  	  		
  		} catch(Exception ex) {
  			
  			String bookingEndpoint = "https://api.little.bz/service/ride/request-ride";
  			JSONObject response = new JSONObject();
  			
  			//added parameter locationId on the request JSONObject
  			//String shopContact = locationService.findByLocationId(details.get("locationId").toString()).getPhoneNumber();
  			String shopContact = locationService.findByLocationId("NAIROBI").getPhoneNumber();
  			
  			String requestJson = "{\r\n" + 
  					"    \"type\": \"CORPORATE\",\r\n" + 
  					"    \"driver\": \""+ com.jaguarpetroleum.JgasRegistration.Configs.Constants.LITTLEDRIVER+"\",\r\n" + 																							
  					"    \"rider\": {\r\n" + 
  					"        \"mobileNumber\": \"254"+shopContact.substring(1)+"\",\r\n" +      
  					"        \"name\": \""+details.get("ferriedPassengerName").toString()+"\",\r\n" + 
  					"        \"email\": \"test@test.com\",\r\n" + 
  					"        \"picture\": \"https://google.com/mypicture.com\"\r\n" + 
  					"    },\r\n" + 
  					"    \"skipDrivers\": [\r\n" + 
  					"        \"person@mail.com\"\r\n" + 
  					"    ],\r\n" + 
  					"    \"vehicle\": {\r\n" + 
  					"        \"type\": \""+com.jaguarpetroleum.JgasRegistration.Configs.Constants.LITTLETYPE+"\",\r\n" + 																											
  					"        \"details\": {\r\n" + 
  					"            \"itemCarried\": \""+details.get("itemCarried").toString()+"\",\r\n" + 
  					"            \"size\": \""+details.get("itemSize").toString()+"\",\r\n" + 
  					"            \"recipientName\": \""+details.get("recipientName").toString()+"\",\r\n" + 
  					"            \"recipientMobile\": \"254"+details.get("recipientMobile").toString().substring(1)+"\",\r\n" +
  					"            \"recipientAddress\": \""+details.get("recipientAddress").toString()+"\",\r\n" + 
  					"            \"contactPerson\": \""+details.get("contactPerson").toString()+"\",\r\n" + 
  					"            \"deliveryNotes\": \""+details.get("deliveryNotes").toString()+"\",\r\n" + 
  					"            \"typeOfAddress\": \""+details.get("typeOfAddress").toString()+"\"\r\n" + 
  					"        }\r\n" + 
  					"    },\r\n" + 
  					"    \"pickUp\": {\r\n" + 
  					"        \"latlng\": \""+details.get("pickUpLatLng").toString()+"\",\r\n" + 
  					"        \"address\": \""+details.get("pickUpAddress").toString()+"\"\r\n" + 
  					"    },\r\n" + 
  					"    \"dropOff\": {\r\n" + 
  					"        \"latlng\": \""+details.get("dropOffLatLng").toString()+"\",\r\n" + 
  					"        \"address\": \""+details.get("recipientAddress").toString()+"\"\r\n" + 
  					"    },\r\n" + 
  					"    \"dropOffs\": [\r\n" + 
  					"        {\r\n" + 
  					"            \"order\": 1,\r\n" + 
  					"            \"address\": \""+details.get("pickUpAddress").toString()+"\",\r\n" + 
  					"            \"latlng\": \""+details.get("pickUpLatLng").toString()+"\",\r\n" + 
  					"            \"contactMobileNumber\": \"254"+shopContact.substring(1)+"\",\r\n" + 
  					"            \"contactName\": \"KNTC-Jaguar Petroleum\",\r\n" + 
  					"            \"notes\": \"Pick "+details.get("itemCarried").toString()+" for customer "+details.get("contactPerson").toString()+" from Jaguar Petroleum. Order Number "+details.get("orderNo").toString()+". Destination "+details.get("recipientAddress").toString()+" \"\r\n" + 
  					"        },\r\n" +
  					"        {\r\n" + 
  					"            \"order\": 2,\r\n" + 
  					"            \"address\": \""+details.get("recipientAddress").toString()+"\",\r\n" + 
  					"            \"latlng\": \""+details.get("dropOffLatLng").toString()+"\",\r\n" + 
  					"            \"contactMobileNumber\": \"254"+details.get("recipientMobile").toString().substring(1)+"\",\r\n" + 
  					"            \"contactName\": \""+details.get("recipientName").toString()+"\",\r\n" + 
  					"            \"notes\": \"Delivery for "+details.get("contactPerson").toString()+" from Jaguar Petroleum for order number "+details.get("orderNo").toString()+"\"\r\n" + 
  					"        },\r\n" + 
  					"        {\r\n" + 
  					"            \"order\": 3,\r\n" + 
  					"            \"address\": \""+details.get("pickUpAddress").toString()+"\",\r\n" + 
  					"            \"latlng\": \""+details.get("pickUpLatLng").toString()+"\",\r\n" + 
  					"            \"contactMobileNumber\": \"254"+shopContact.substring(1)+"\",\r\n" + 
  					"            \"contactName\": \"KNTC-Jaguar Petroleum\",\r\n" + 
  					"            \"notes\": \"Return cage for order number "+details.get("orderNo").toString()+" to Jaguar Petroleum\"\r\n" + 
  					"        }\r\n" + 
  					"    ],\r\n" + 
  					"    \"corporate\": {\r\n" + 
  					"        \"corporateId\": \""+com.jaguarpetroleum.JgasRegistration.Configs.Constants.LITTLECORPORATEID+"\"\r\n" + 																											
  					"    }\r\n" + 
  					"}";					
  			
  			logger.info("Rider booking Payload: "+ requestJson);
  			
  			HttpHeaders headers = new HttpHeaders();
  			headers.setContentType(MediaType.APPLICATION_JSON);	
  			headers.setBearerAuth(generateToken().get("token").toString());				
  			
  			HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);		
  			
  			disableSslVerification();	    
  			URI uri = new URI(bookingEndpoint);  			
  			
  			JSONParser parser = new JSONParser();  
  			
  			try {
  				response = (JSONObject) parser.parse(restTemplate.exchange(uri 
  																, HttpMethod.POST, entity, String.class).getBody());
  								 
  			} catch (ParseException e) {				
  				logger.error(e.getLocalizedMessage());
  			} 			
  			
  			logger.info("LittleCab Booking Response Payload "+ response);			
  			
  			response.appendField("orderNo", details.get("orderNo").toString());
  			response.appendField("provider", "LittleCab");

  			return parseBookRide(response);
  		}		
		
	}
	
	@GetMapping("/rideStatus/{tripId}")
	public JSONObject rideStatus(@PathVariable String tripId) throws RestClientException, ParseException {
		JSONObject response = new JSONObject();	
		if(!tripId.trim().isEmpty()) {
			
			if(rideService.findByTripId(tripId).getProvider().toString().equals("J-Mobility")) {
				String statusEndpoint = "http://89.38.97.47:3001/user-sessions/booking/v1/trip/status?tripId="+tripId;
				logger.info("Request to check ride status "+statusEndpoint);				
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);	
				//headers.setBearerAuth(generateToken().get("token").toString());
				HttpEntity <String> entity = new HttpEntity<String>(headers);
			      
				JSONParser parser = new JSONParser(); 
				disableSslVerification();
				try {
					JSONObject resp = (JSONObject) parser.parse(restTemplate.exchange(statusEndpoint , HttpMethod.GET, entity, String.class).getBody());
					
					Map trip = (Map) resp.get("trip");
	  	  			JSONObject tripDetails = new JSONObject(trip);
	  	  			
	  	  			String stat = "";
	  	  			
	  	  		switch (trip.get("tripStatus").toString().toUpperCase()) {
	            case "START":
	            	stat = "STARTED";   
	            	break;
	            case "STARTED":
	            	stat = "STARTED";   
	            	break;
	            case "ACCEPTED":
	            	stat = "RIDER ASSIGNED";   
	            	break;
	            case "ONGOING":
	            	stat = "STARTED";   
	            	break;
	            case "ENDED":
	            	stat = "ENDED";
	            	break;
	            case "END":
	            	stat = "ENDED";
	            	break;
	            case "CANCELLED":
	            	stat = "CANCELLED";
	            	break;
	            case "DELIVERED":
	            	stat = "DELIVERED";
	            	break;
	            	//default :
	            	//stat = "STARTED";
	            }
					
					String payload = "{\r\n" + 
							"  \"country\": \"KENYA\",\r\n" + 
							"  \"endedOn\": \"2024-04-12T19:07:38.503\",\r\n" + 
							"  \"city\": \"NAIROBI\",\r\n" + 
							"  \"dropOff\": {\r\n" + 
							"    \"actual\": {\r\n" + 
							"      \"address\": \"NAIROBI\",\r\n" + 
							"      \"latlng\": \""+trip.get("dropOffLatitude").toString()+","+trip.get("dropOffLongitude").toString()+"\"\r\n" + 
							"    },\r\n" + 
							"    \"initial\": {\r\n" + 
							"      \"address\": \"NAIROBI\",\r\n" + 
							"      \"latlng\": \""+trip.get("pickupLatitude").toString()+","+trip.get("pickupLongitude").toString()+"\"\r\n" + 
							"    }\r\n" + 
							"  },\r\n" + 
							"  \"extraDropOffDetails\": [\r\n" + 
							"    {\r\n" + 
							"      \"contactMobileNumber\": \"254792622345\",\r\n" + 
							"      \"address\": \"NAIROBI\",\r\n" + 
							"      \"notes\": \"Pick Delivery for customer NAIROBI from KNTC-Jaguar Petroleum. Order Number LLSQU7HQ. Destination 1st floor \",\r\n" + 
							"      \"contactName\": \"KNTC-Jaguar Petroleum\",\r\n" + 
							"      \"isEnded\": false,\r\n" + 
							"      \"latlng\": \""+trip.get("pickupLatitude").toString()+","+trip.get("pickupLongitude").toString()+"\",\r\n" + 
							"      \"order\": 1\r\n" + 
							"    },\r\n" + 
							"    {\r\n" + 
							"      \"contactMobileNumber\": \"254711791877\",\r\n" + 
							"      \"address\": \"1st floor\",\r\n" + 
							"      \"notes\": \"Delivery for NAIROBI from Jaguar Petroleum for order number LLSQU7HQ\",\r\n" + 
							"      \"contactName\": \"betty \",\r\n" + 
							"      \"isEnded\": false,\r\n" + 
							"      \"latlng\": \""+trip.get("dropOffLatitude").toString()+","+trip.get("dropOffLongitude").toString()+"\",\r\n" + 
							"      \"order\": 2\r\n" + 
							"    },\r\n" + 
							"    {\r\n" + 
							"      \"contactMobileNumber\": \"254792622345\",\r\n" + 
							"      \"address\": \"NAIROBI\",\r\n" + 
							"      \"notes\": \"Return cage for order number LLSQU7HQ to KNTC-Jaguar Petroleum\",\r\n" + 
							"      \"contactName\": \"KNTC-Jaguar Petroleum\",\r\n" + 
							"      \"isEnded\": false,\r\n" + 
							"      \"latlng\": \""+trip.get("pickupLatitude").toString()+","+trip.get("pickupLongitude").toString()+"\",\r\n" + 
							"      \"order\": 3\r\n" + 
							"    }\r\n" + 
							"  ],\r\n" + 
							"  \"pickUp\": {\r\n" + 
							"    \"actual\": {\r\n" + 
							"      \"address\": \"Yallow Road\",\r\n" + 
							"      \"latlng\": \"\"\r\n" + 
							"    },\r\n" + 
							"    \"initial\": {\r\n" + 
							"      \"address\": \"NAIROBI\",\r\n" + 
							"      \"latlng\": \""+trip.get("pickupLatitude").toString()+","+trip.get("pickupLongitude").toString()+"\"\r\n" + 
							"    }\r\n" + 
							"  },\r\n" + 
							"  \"tripId\": \""+trip.get("tripId").toString()+"\",\r\n" + 
							"  \"rider\": \""+trip.get("riderId").toString()+"\",\r\n" + 
							"  \"corporateId\": \"65c0d192e22e5\",\r\n" + 
							"  \"vehicle\": {\r\n" + 
							"    \"meta\": {\r\n" + 
							"      \"min\": 200,\r\n" + 
							"      \"perKM\": 0,\r\n" + 
							"      \"perMin\": 0,\r\n" + 
							"      \"base\": 0\r\n" + 
							"    },\r\n" + 
							"    \"location\": {\r\n" + 
							"      \"bearing\": \"238.915924072265625\",\r\n" + 
							"      \"latlng\": \""+trip.get("riderLatitude").toString()+","+trip.get("riderLongitude").toString()+"\"\r\n" + 
							"    },\r\n" + 
							"    \"type\": \"PARCELS\"\r\n" + 
							"  },\r\n" + 
							"  \"driver\": \"kipomarichard12@gmail.com\",\r\n" + 
							"  \"tripCost\": 770,\r\n" + 
							"  \"currency\": \"KES\",\r\n" + 
							"  \"payment\": {\r\n" + 
							"    \"mode\": \"CORPORATE\",\r\n" + 
							"    \"reference\": \"\",\r\n" + 
							"    \"amount\": 770,\r\n" + 
							"    \"currency\": \"KES\"\r\n" + 
							"  },\r\n" + 
							"  \"metrics\": {\r\n" + 
							"    \"cost\": {\r\n" + 
							"      \"total\": 770,\r\n" + 
							"      \"liveFare\": 770\r\n" + 
							"    },\r\n" + 
							"    \"distance\": {\r\n" + 
							"      \"total\": 28.27,\r\n" + 
							"      \"cost\": 770\r\n" + 
							"    },\r\n" + 
							"    \"otp\": {\r\n" + 
							"      \"parking\": \""+trip.get("deliveryCode").toString()+"\",\r\n" + 
							"      \"startTrip\": \""+trip.get("startCode").toString()+"\",\r\n" + 
							"      \"endTrip\": \""+trip.get("endCode").toString()+"\"\r\n" + 
							"    },\r\n" + 
							"    \"time\": {\r\n" + 
							"      \"total\": 89,\r\n" + 
							"      \"cost\": 0\r\n" + 
							"    }\r\n" + 
							"  },\r\n" + 
							"  \"startedOn\": \"2024-04-12T17:37:43.703\",\r\n" + 
							"  \"tripStatus\": \""+stat+"\",\r\n" + 
							"  \"status\": {\r\n" + 
							"    \"driverStatus\": \""+stat+"\",\r\n" + 
							"    \"driverStatusId\": 7,\r\n" + 
							"    \"tripStatus\": \""+stat+"\"\r\n" + 
							"  }\r\n" + 
							"}";
					
					response  = (JSONObject) JSONValue.parse(payload);
									 
				} catch (ParseException e) {				
					logger.error("Error getting trip status. Details: "+e.getMessage());
				}
				
			} else {
				String statusEndpoint = "https://api.little.bz/service/ride/"+tripId+"/status";
				logger.info("Request to check ride status "+statusEndpoint);				
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);	
				headers.setBearerAuth(generateToken().get("token").toString());
				HttpEntity <String> entity = new HttpEntity<String>(headers);
			      
				JSONParser parser = new JSONParser(); 
				disableSslVerification();
				try {
					response = (JSONObject) parser.parse(restTemplate.exchange(statusEndpoint , HttpMethod.GET, entity, String.class).getBody());
									 
				} catch (ParseException e) {				
					logger.error("Error getting trip status. Details: "+e.getMessage());
				}
			}
			
			
		} else {
			response.put("resultMessage", "The tripId is missing");
			response.put("resultCode", 10010);
		}	
		
		logger.info("Ride status response "+response);
		return parseRideStatus(response);
	}
	
	@GetMapping("/cancelRide/{tripId}")
	public JSONObject cancelRide(@PathVariable String tripId) throws RestClientException, ParseException, URISyntaxException {
		JSONObject response = new JSONObject();
		if(!tripId.trim().isEmpty()) {
			
			if(rideService.findByTripId(tripId).getProvider().toString().equals("J-Mobility")) {
				String cancelEndpoint = "http://89.38.97.47:5001/v1/customer/cancelTrip";
				logger.info("Request to cancel ride  "+cancelEndpoint);		
				
				String requestJson = "{\r\n" + 
						"    \"tripId\": \""+tripId+"\"\r\n" + 
						"}";		
				
				logger.info("Cancel Ride Payload: "+ requestJson);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);	
				//headers.setBearerAuth(generateToken().get("token").toString());				
				
				HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);		
				
				disableSslVerification();	    
				URI uri = new URI(cancelEndpoint);  			
				
				JSONParser parser = new JSONParser();  
				disableSslVerification();
				
				try {
					response = (JSONObject) parser.parse(restTemplate.exchange(uri 
																	, HttpMethod.POST, entity, String.class).getBody());
									 
				} catch (ParseException e) {				
					logger.error("Error cancelling ride "+tripId+". Details "+e.getMessage());
					
					response.put("resultMessage", "Error cancelling ride "+tripId+". Details "+e.getMessage());
					response.put("resultCode", 20010);
				} 
			} else {
				String cancelEndpoint = "https://api.little.bz/service/ride/"+tripId+"/cancel";
				logger.info("Request to cancel ride  "+cancelEndpoint);		
				
				String requestJson = "{\r\n" + 
						"    \"reason\": \"Wrong Request\"\r\n" + 
						"}";		
				
				logger.info("Cancel Ride Payload: "+ requestJson);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);	
				headers.setBearerAuth(generateToken().get("token").toString());				
				
				HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);		
				
				disableSslVerification();	    
				URI uri = new URI(cancelEndpoint);  			
				
				JSONParser parser = new JSONParser();  
				disableSslVerification();
				
				try {
					response = (JSONObject) parser.parse(restTemplate.exchange(uri 
																	, HttpMethod.POST, entity, String.class).getBody());
									 
				} catch (ParseException e) {				
					logger.error("Error cancelling ride "+tripId+". Details "+e.getMessage());
					
					response.put("resultMessage", "Error cancelling ride "+tripId+". Details "+e.getMessage());
					response.put("resultCode", 20010);
				} 
			}
				
		} else {
			response.put("resultMessage", "The tripId is missing");
			response.put("resultCode", 10010);
		}				
		
		logger.info("LittleCab cancel ride response "+ response);	
		rideService.updateStatus("CANCELLED", tripId);
		
		return response;
	}
	
	@PostMapping("/vendorBookRide/{orderNo}")
	public JSONObject vendorBookRide(@PathVariable String orderNo) throws RestClientException, ParseException, URISyntaxException {
		logger.info("Received vendor request to book ride for order no "+orderNo);
		JSONObject request = new JSONObject();
		OrderHD customerOrder = new OrderHD();
		Registration customerRegDetails = new Registration();
		Location location = new Location();
		
		customerOrder = orderHDService.findByOrderNo(orderNo);
		customerRegDetails = registrationService.findByPhoneNumber(customerOrder.getPhoneNumber());
		location = locationService.findByLocationId(customerOrder.getLocationId());
		
		request.put("ferriedPassengerNumber", location.getPhoneNumber());
		request.put("ferriedPassengerName", location.getLocationDescrition());
		request.put("itemCarried", "LPG Gas");
		request.put("itemSize", "1");
		request.put("recipientName", customerOrder.getRecepientName());
		request.put("recipientMobile", customerOrder.getRecepientPhone());
		request.put("recipientAddress", customerOrder.getCustomerLocationName());
		request.put("contactPerson", customerRegDetails.getFirstName()+" "+customerRegDetails.getLastName());
		request.put("deliveryNotes", "Delivery Order Number "+ customerOrder.getOrderNo());
		request.put("typeOfAddress", "Home");
		request.put("pickUpLatLng", customerOrder.getPickUpLatitude()+","+customerOrder.getPickUpLongitude());
		request.put("pickUpAddress", location.getLocationDescrition());
		request.put("dropOffLatLng", customerOrder.getDestinationLatitude()+","+customerOrder.getDestinationLongitude());
		request.put("dropOffAddress", customerOrder.getSpecificLocation());
		request.put("notes", "LPG Delivery");
		request.put("orderNo", customerOrder.getOrderNo());
		request.put("locationId", customerOrder.getLocationId());
		
		return bookRide(request);
	}
	
	private void disableSslVerification() {        
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts;
            trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            //e.printStackTrace();
            logger.error(e.toString());
        }
    }
	
	public JSONObject parseBookRide(JSONObject bookRideResponse) {
		JSONObject response = new JSONObject();
        try {
            // Parse the JSON payload
        	/*
				 {
				  "distance": "0.00",
				  "driver": {
				    "name": "Phillip",
				    "mobile": "254759490441",
				    "rating": "4.97",
				    "email": "",
				    "picture": "https://littleimages.blob.core.windows.net/driverimages/ethiopia/phillip.otieno@little.bz.jpg"
				  },
				  "car": {
				    "color": "Black",
				    "distance": "0.0",
				    "model": "TOYOTA MASTER",
				    "plate": "KBC 300C",
				    "time": "1",
				    "latlng": "0.000000000000000,0.000000000000000"
				  },
				  "tripId": "F63E0487-0BB2-400F-967D-0C5D71DA6006-2024-02",
				  "time": "0.00"
				}
        	 */
            org.json.JSONObject payloadObject = new org.json.JSONObject(bookRideResponse.toString());
            String tripId = payloadObject.getString("tripId");
            
            org.json.JSONObject driver = payloadObject.getJSONObject("driver");
            String driverName = driver.getString("name");
            String driverMobile = driver.getString("mobile");
            
            org.json.JSONObject car = payloadObject.getJSONObject("car");
            String color = car.getString("color");
            String model = car.getString("model");
            String plate = car.getString("plate");
            
            response.put("tripId", tripId);
            response.put("driverName", driverName);
            response.put("driverMobile", driverMobile);
            response.put("color", color);
            response.put("model", model);
            response.put("plate", plate);
            response.put("resultCode", 0);
            response.put("resultMessage", "Success in finding rider");
            
            //save rider details to DB
            Ride ride = new Ride();
            ride.setColor(color);
            ride.setDriverMobile(driverMobile);
            ride.setDriverName(driverName);
            ride.setModel(model);
            ride.setPlate(plate);
            ride.setOrderNo(bookRideResponse.getAsString("orderNo"));
            ride.setTripId(tripId);
            ride.setStatus("RIDER ASSIGNED");
            ride.setDeliveryCodeSent(0);
            if(bookRideResponse.containsKey("startCode")) {
            	ride.setStartCode(bookRideResponse.getAsString("startCode"));
            }
            if(bookRideResponse.containsKey("endCode")) {
            	ride.setEndCode(bookRideResponse.getAsString("endCode"));
            }
            ride.setProvider(bookRideResponse.getAsString("provider"));
            
            rideService.save(ride);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("resultCode", 10010);
            response.put("resultMessage", "Error in finding rider");
        }
        return response;
    }
	
	public JSONObject parseRideStatus(JSONObject rideStatusResponse) {
		JSONObject response = new JSONObject();
        try {
            org.json.JSONObject payloadObject = new org.json.JSONObject(rideStatusResponse.toString());
            String tripId = payloadObject.getString("tripId");
            String tripStatus = payloadObject.getString("tripStatus");
            String city = payloadObject.getString("city");
            
            org.json.JSONObject vehicle = payloadObject.getJSONObject("vehicle").getJSONObject("location");
            String vehicleLatlong = vehicle.getString("latlng");
            
            org.json.JSONObject codes = payloadObject.getJSONObject("metrics").getJSONObject("otp");
            String startCode = codes.getString("startTrip");
            String endCode = codes.getString("endTrip");
            String parking = codes.getString("parking");
            
            org.json.JSONObject deliveryDone = payloadObject.getJSONArray("extraDropOffDetails").getJSONObject(1);
            boolean isEnded = deliveryDone.getBoolean("isEnded");
            
            org.json.JSONObject pickUp = payloadObject.getJSONObject("pickUp").getJSONObject("initial");
            String pickUpLatlong = pickUp.getString("latlng");
            
            org.json.JSONObject destination = payloadObject.getJSONObject("dropOff").getJSONObject("initial");
            String destinationLatlong = destination.getString("latlng");
            
            response.put("tripId", tripId);
            response.put("tripStatus", tripStatus);
            response.put("city", city);
            response.put("vehicleLatlong", vehicleLatlong);
            response.put("pickUpLatlong", pickUpLatlong);
            response.put("destinationLatlong", destinationLatlong);
            response.put("startCode", startCode);
            response.put("endCode", (isEnded == true) ? endCode : "");
            response.put("parking", parking);
            response.put("deliveryMade", isEnded);
            response.put("resultCode", 0);
            response.put("resultMessage", "Success in finding ride status");
            response.put("riderName", rideService.findByTripId(tripId).getDriverName());
            response.put("riderContact", rideService.findByTripId(tripId).getDriverMobile());
            response.put("plate", rideService.findByTripId(tripId).getPlate());            
            
            //Only present the end code to the vndor if the delivery has been made to the customer
           /*
            *  if(isEnded == true) {
            	rideService.updateCode(startCode, endCode, tripId, parking);
            } else {
            	rideService.updateCode(startCode, "", tripId, parking);
            }    
            */
            
            rideService.updateCode(startCode, endCode, tripId, parking);            
            rideService.updateStatus(tripStatus, tripId);
            
            //Code block below udates the order status based on the littleCab status
            String myStatus = null;
            switch (tripStatus) {
            case "STARTED":
            	myStatus = "Rider Enroute";
            	//check if delivery code has been sent. if not send the parking code
            	
            	if(rideService.findByTripId(tripId).getDeliveryCodeSent() == 0) {
            		AfricasTalkingIntegration africasTalking = new AfricasTalkingIntegration();
            		
                	africasTalking.sendSms(orderHDService.findByOrderNo(rideService.findByTripId(tripId).getOrderNo()).getCustomerNumber(), 
                    		"Dear Customer, your order has been dispatched. "+parking+" is your delivery code. Please share this code with the delivery agent to complete the delivery process. J-Gas");                    
            	
                	rideService.updateDeliveryCodeStatus(tripId);
            	}   
            	break;
            case "ENDED":
            	myStatus = "Order Delivered";
            	break;
            case "RATED":
            	myStatus = "Order Delivered";
            	break;
            case "DELIVERED":
            	myStatus = "Goods Received";
            	break;
            	default :
            	myStatus = "Order Processing";
            }
            
            myStatus = (isEnded == true && myStatus == "Rider Enroute")? "Goods Received" : myStatus;
            
            orderHDService.updateStatus(myStatus, rideService.findByTripId(tripId).getOrderNo());

        } catch (Exception e) {
            e.printStackTrace();
            response.put("resultCode", 10010);
            response.put("resultMessage", "Error in finding ride status. "+e.getMessage());
        }
        return response;
    }
}
