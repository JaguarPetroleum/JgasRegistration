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
		String messageUrl = "https://fcm.googleapis.com/fcm/send";

		// Set your server key
		String serverKey = "AAAAHrMsEng:APA91bF70BPdImh1N10J3rQB4PcxzzUOB2MQEduB5lVI57ZHGkRQfj4flRjx4OD42gN64Q_JtIA3MPUh9X1H964CWRIBNK8udJDuYaVfc2Wq-BiaAYbxb0t9jeDfXKdrwG-aMsdQRD8r";
		String senderId = "131855028856";
		
		// Construct your message payload (customize as needed)
		String messagePayload = "{"
		    + "\"data\": {"
		    + "\"orderNo\": \""+notification.get("orderNo").toString()+"\","
    		+ "\"customerName\": \""+notification.get("customerName").toString()+"\","
			+ "\"location\": \""+notification.get("location").toString()+"\","
		    + "\"productName\": \""+notification.get("productName").toString()+"\""
		    + "},"
		    + "\"to\": \""+senderId+"\""
		    + "}";

		logger.info("FCM notification payload "+messagePayload);
		// Create an HTTP POST request
		HttpPost request = new HttpPost(messageUrl);
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", "key=" + serverKey);
		request.setEntity(new StringEntity(messagePayload));

		// Execute the request
		try {
		    HttpResponse response = httpClient.execute(request);
		    // Handle the response (e.g., check for success or error)
		    // ...
		    logger.info("FCM response "+response.toString());
		} catch (IOException e) {
		    e.printStackTrace();
		    logger.error("FCM error "+e.getMessage());
		}
	}

}
