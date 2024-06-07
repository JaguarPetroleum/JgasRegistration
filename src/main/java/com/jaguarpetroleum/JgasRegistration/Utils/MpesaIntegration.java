package com.jaguarpetroleum.JgasRegistration.Utils;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.jaguarpetroleum.JgasRegistration.Service.LocationService;
import com.jaguarpetroleum.JgasRegistration.Service.OrderHDService;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;

import org.apache.http.impl.client.HttpClients;

@RestController
public class MpesaIntegration {
	private static final Logger logger = LoggerFactory.getLogger(MpesaIntegration.class);
	@Autowired
	OrderHDService orderHDService;
	@Autowired
	LocationService locationService;
	@Autowired
	LittleCabIntegration littleCabIntegration;
	
	FirebaseIntegration fbi = new FirebaseIntegration();
	
	@PostMapping("/stkPush")
	public JSONObject stkPush(@RequestBody JSONObject details) {
		logger.info("Received STKPush request "+details);
		JSONObject res = new JSONObject();		

		String consumerKey = com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESACONSUMERKEY; 
        String consumerSecret = com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESACONSUMERSECRET; 
        String shortcode = com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESASHORTCODE;
        String passkey = com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESAPASSKEY; 
        String requestBody = null;
        
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESASTKPUSHENDPOINT);

            String auth = consumerKey + ":" + consumerSecret;
            String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
            logger.info("Sending token request");
            request.addHeader("Authorization", "Bearer " + auth().get("access_token").toString());
            logger.info("Token received");
            request.addHeader("Content-Type", "application/json");

            String ts = getTimestamp() ;
            requestBody = "{ \"BusinessShortCode\": \"" + shortcode + "\", " +
                                    "\"Password\": \"" + getPassword(shortcode, passkey) + "\", " +
                                    "\"Timestamp\": \"" + getTimestamp() + "\", " +
                                    "\"TransactionType\": \"CustomerPayBillOnline\", " +
                                    "\"Amount\": \"" + details.get("amount").toString() + "\", " +
                                    "\"PartyA\": \"254" + details.get("phoneNumber").toString().substring(1)+ "\", " +
                                    "\"PartyB\": \"" + shortcode + "\", " +
                                    "\"PhoneNumber\": \"254" + details.get("phoneNumber").toString().substring(1) + "\", " +
                                    "\"CallBackURL\": \""+com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESASTKCALLBACKENDPOINT+"\", " +		
                                    "\"AccountReference\": \""+details.get("accountReference").toString() +"\", " +
                                    "\"TransactionDesc\": \""+details.get("accountReference").toString()+"\" }";
            
            logger.info("STKpush Request Payload "+requestBody);

            request.setEntity(new StringEntity(requestBody));

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();   
                		
            if (entity != null) {
            	JSONParser parser = new JSONParser(); 
            	res = (JSONObject) parser.parse(EntityUtils.toString(entity));
                logger.info("STKPush response "+res);
                
                if(res.containsKey("ResponseCode") && res.get("ResponseCode").toString().equals("0")) {
                	logger.info("Updating order "+details.get("accountReference").toString() +" with STKpush reference");                	
                	orderHDService.updateCheckout(res.get("CheckoutRequestID").toString(), details.get("accountReference").toString());
                	logger.info("Order "+details.get("accountReference").toString()+" successfully udated with checkout ID "+res.get("CheckoutRequestID").toString());
                }
            }
        } catch (Exception ex) {
            logger.error("Error sending STKPush request. Details: "+ex.getMessage()+" on request body "+requestBody );
        }        
        return res;
	}
        
        private static String getPassword(String shortcode, String passkey) {
            String timestamp = getTimestamp();
            return java.util.Base64.getEncoder().encodeToString((shortcode + passkey + timestamp).getBytes());
        }

        private static String getTimestamp() {
            return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        }
        
        @PostMapping("/stkCallback")
        public void stkCallback(@RequestBody JSONObject callbackDetails) {
        	logger.info("Received STKPush callback "+callbackDetails);
            String jsonPayload = callbackDetails.toJSONString();

            try {
                // Parse the JSON payload
                org.json.JSONObject payloadObject = new org.json.JSONObject(jsonPayload);
                org.json.JSONObject stkCallback = payloadObject.getJSONObject("Body").getJSONObject("stkCallback");

                // Extract information from the callback
                String merchantRequestId = stkCallback.getString("MerchantRequestID");
                String checkoutRequestId = stkCallback.getString("CheckoutRequestID");
                int resultCode = stkCallback.getInt("ResultCode");
                String resultDesc = stkCallback.getString("ResultDesc");

                // Access callback metadata
                org.json.JSONObject metadata = stkCallback.getJSONObject("CallbackMetadata");
                org.json.JSONObject amountObject = metadata.getJSONArray("Item").getJSONObject(0);
                double amount = amountObject.getDouble("Value");
                org.json.JSONObject transactionRefObject = metadata.getJSONArray("Item").getJSONObject(1);
                String transactionRef = transactionRefObject.getString("Value");
                org.json.JSONObject phoneNumberObject = metadata.getJSONArray("Item").getJSONObject(4);
                String phoneNumber = phoneNumberObject.get("Value").toString();

                // Print the extracted information
                logger.info("STKPush callback details update on the order details ");
                
              //Update the order record with payment details
                orderHDService.updatePaymentDetails(checkoutRequestId, transactionRef, (resultCode == 0) ? 1 : 0, resultDesc, (resultCode == 0) ? "Order Successful" : "Payment Processing Failed");
               
                if(resultCode == 0) {
                	String orderNo;
                	orderNo = orderHDService.findByCheckOutId(checkoutRequestId).getOrderNo();
                	AfricasTalkingIntegration africasTalking = new AfricasTalkingIntegration();
                	africasTalking.sendSms(phoneNumber, 
                    		"Dear Customer, we have received your payment for your gas order no. "+orderNo+". We are currently processing it. ");
                                    	
                    logger.info("STKPush payment received and processed. Booking a ride in progress");
                  //Added on 23rd Apr 2024, to ensure rider is sought after gas payment is received
                    littleCabIntegration.vendorBookRide(orderNo);
                	//============================================================================
                	
                    JSONObject fbiNotification = new JSONObject();
                    fbiNotification.put("orderNo", orderHDService.findByCheckOutId(checkoutRequestId).getOrderNo());
                    fbiNotification.put("location", orderHDService.findByCheckOutId(checkoutRequestId).getCustomerLocationName());
                    fbiNotification.put("customerName", orderHDService.findByCheckOutId(checkoutRequestId).getCustomerName());
                    fbiNotification.put("productName", orderHDService.findByCheckOutId(checkoutRequestId).getCheckOutRequestId());
                    fbiNotification.put("token", locationService.findByLocationId(orderHDService.findByCheckOutId(checkoutRequestId).getLocationId()).getDeviceToken());
                                        
                    fbi.sendNotification(fbiNotification);
                }                                
                 
            } catch (Exception e) {
            	logger.error("Error on STKpush callback. Details: "+e.getMessage());
                e.printStackTrace();
            }
        }        
        
        @GetMapping("/mpesaToken")
        public static JSONObject auth() throws ParseException, net.minidev.json.parser.ParseException {        		
        		// Define the endpoint for generating access token
                String tokenEndpoint = com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESATOKENENDPOINT;

                // Define your consumer key and consumer secret
                String consumerKey = com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESACONSUMERKEY;
                String consumerSecret = com.jaguarpetroleum.JgasRegistration.Configs.Constants.MPESACONSUMERSECRET;

                // Create the authentication string
                String authString = consumerKey + ":" + consumerSecret;

                // Create HTTP client
                HttpClient httpClient = HttpClients.createDefault();

                // Create HTTP POST request
                HttpGet httpGet = new HttpGet(tokenEndpoint);

                // Set headers
                httpGet.addHeader("Content-Type", "application/json");
                httpGet.addHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(authString.getBytes()));
                try {
                    // Execute the request
                    HttpResponse response = httpClient.execute(httpGet);

                    // Print response
                    logger.info("Token generation request httpStatus code : " + response.getStatusLine().getStatusCode());
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity);                    
                    logger.info("Response : " + responseString);
                    
                    JSONObject jsonObject = (JSONObject) JSONValue.parse(responseString);

                    return jsonObject;
                    
                } catch (IOException e) {
                    logger.error("Error encountered generating Mpesa access token. Details: "+ e.getMessage());         
                    return null;           
                }
                
        }
        
        /*
         * {    
   "TransactionType": "Pay Bill",
   "TransID":"RKTQDM7W6S",
   "TransTime":"20191122063845",
   "TransAmount":"10"
   "BusinessShortCode": "600638",
   "BillRefNumber":"invoice008",
   "InvoiceNumber":"",
   "OrgAccountBalance":""
   "ThirdPartyTransID": "",
   "MSISDN":"25470****149",
   "FirstName":"John",
   "MiddleName":""
   "LastName":"Doe"
}
         */
        
    	@PostMapping("/c2bValidation")
    	public JSONObject c2bValidation(@RequestBody JSONObject details) {
    		logger.info("Received c2b validation request "+details);
    		JSONObject res = new JSONObject();    		
    		
    		if(orderHDService.findByOrderNoAndAmount(details.get("BillRefNumber").toString(), details.get("TransAmount").toString()) != null) {
    			logger.info("Order validation successful ");
    			res.put("ResultCode", "0");
    			res.put("ResultDesc", "Accepted");
    			res.put("ThirdPartyTransID", details.get("BillRefNumber").toString());
    		} else {
    			logger.warn("Order validation failed ");
    			res.put("ResultCode", "C2B00016");
    			res.put("ResultDesc", "Rejected");
    		}
			return res;
    	}
    	
    	@PostMapping("/c2bNotification")
    	public JSONObject c2bNotification(@RequestBody JSONObject details) throws IOException, RestClientException, net.minidev.json.parser.ParseException, URISyntaxException {
    		logger.info("Received c2b payment notification  "+details);
    		JSONObject res = new JSONObject();
    		
    		//Update the order record with payment details
    		if(orderHDService.findByOrderNo(details.getAsString("ThirdPartyTransID").toString()).getPaid()==0) {
    			orderHDService.updatePaymentDetailsC2B( details.getAsString("ThirdPartyTransID").toString(), 
                		details.getAsString("TransID").toString(), 
                		1, 
                		details.getAsString("TransactionType").toString(), 
                		"Order Successful");
                
                AfricasTalkingIntegration africasTalking = new AfricasTalkingIntegration();
                
                africasTalking.sendSms(orderHDService.findByOrderNo(details.getAsString("ThirdPartyTransID").toString()).getPhoneNumber(), 
                		"Dear Customer, we have received your payment for your gas order no. "+details.getAsString("ThirdPartyTransID").toString()+". We are currently processing it. ");
                
                JSONObject fbiNotification = new JSONObject();
                fbiNotification.put("orderNo", orderHDService.findByOrderNo(details.getAsString("ThirdPartyTransID").toString()).getOrderNo());
                fbiNotification.put("location", orderHDService.findByOrderNo(details.getAsString("ThirdPartyTransID").toString()).getCustomerLocationName());
                fbiNotification.put("customerName", orderHDService.findByOrderNo(details.getAsString("ThirdPartyTransID").toString()).getCustomerName());
                fbiNotification.put("productName", orderHDService.findByOrderNo(details.getAsString("ThirdPartyTransID").toString()).getCheckOutRequestId());
                fbiNotification.put("token", locationService.findByLocationId(orderHDService.findByOrderNo(details.getAsString("ThirdPartyTransID").toString()).getLocationId()).getDeviceToken());
                
                fbi.sendNotification(fbiNotification);                
                
                //LittleCabIntegration littleCabIntegration = new LittleCabIntegration();
                logger.info("Paybill payment received and processed. Booking a ride in progress");
                
                littleCabIntegration.vendorBookRide(details.getAsString("ThirdPartyTransID").toString());
                
        		logger.info("Order has been successfully paid for and processed.");
    		}            
			return res;
    	}

    }
        
  
