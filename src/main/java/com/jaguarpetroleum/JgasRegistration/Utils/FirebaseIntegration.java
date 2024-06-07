package com.jaguarpetroleum.JgasRegistration.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
public class FirebaseIntegration {
	private static final Logger logger = LoggerFactory.getLogger(FirebaseIntegration.class);
	
	@PostMapping("/fcmNotification")
	public void sendNotification(@RequestBody JSONObject notification) throws UnsupportedEncodingException {
		logger.info("Received request to send FCM notification "+notification);
		// Create an HttpClient instance
		HttpClient httpClient = HttpClientBuilder.create().build();

		// Define the FCM endpoint URL
		String messageUrl = "http://89.38.97.47:5001/v1/notifications/send/push";
		
		// Construct your message payload (customize as needed)
		String messagePayload = "{\r\n"
				+ "    \"token\": \""+notification.get("token").toString()+"\",\r\n"
				+ "    \"title\": \"New Order Notification\",\r\n"
				+ "    \"body\": \"Customer Name: "+notification.get("customerName").toString()
								+ " Order No: "+ notification.get("orderNo").toString() 
								+ ". Product: "+ notification.get("productName").toString() 
								+ ". Location: "+ notification.get("location").toString() + "\",\r\n"
				+ "    \"product\": \"JGAS\"\r\n"
				+ "}";

		logger.info("FCM notification payload "+messagePayload);
		// Create an HTTP POST request
		HttpPost request = new HttpPost(messageUrl);
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(messagePayload));

		// Execute the request
		try {
		    HttpResponse response = httpClient.execute(request);
		    logger.info("FCM response "+response.toString());
		} catch (IOException e) {
		    e.printStackTrace();
		    logger.error("FCM error "+e.getMessage());
		}
	}

}
