package com.jaguarpetroleum.JgasRegistration.Utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClientException;
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
		
		String url = "http://89.38.97.47:5001/v1/trip/tripinfo";		
		
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
		
		String url = "http://89.38.97.47:5001/v1/customer/request/";		
		
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
	
	public JSONObject rideStatus(@PathVariable String tripId) throws RestClientException, ParseException {
		JSONObject response = new JSONObject();	
		if(tripId != null ) {
			String statusEndpoint = "http://89.38.97.47:3001/user-sessions/booking/v1/trip/status?tripId="+tripId;
			logger.info("Request to check ride status "+statusEndpoint);				
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);	
			//headers.setBearerAuth(generateToken().get("token").toString());
			HttpEntity <String> entity = new HttpEntity<String>(headers);
		      
			JSONParser parser = new JSONParser(); 
			disableSslVerification();
			try {
				response = (JSONObject) parser.parse(restTemplate.exchange(statusEndpoint , HttpMethod.GET, entity, String.class).getBody());
								 
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		} else {
			response.put("resultMessage", "The tripId is missing");
			response.put("resultCode", 10010);
		}	
		
		logger.info("Ride status response "+response);
		//return parseRideStatus(response);
		return response;
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
}
