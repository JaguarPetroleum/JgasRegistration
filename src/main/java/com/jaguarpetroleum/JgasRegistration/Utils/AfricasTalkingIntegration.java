package com.jaguarpetroleum.JgasRegistration.Utils;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;

public class AfricasTalkingIntegration {
	private static final Logger logger = LoggerFactory.getLogger(AfricasTalkingIntegration.class);
		public void sendSms(String phoneNumber, String messageContent) throws IOException {
 	        AfricasTalking.initialize(com.jaguarpetroleum.JgasRegistration.Configs.Constants.ATUSERNAME, 
 	        									com.jaguarpetroleum.JgasRegistration.Configs.Constants.ATAPIKEY);
 	       
	        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
	        String formatNumber=phoneNumber.substring(phoneNumber.length() - 9, phoneNumber.length());
	        String formatedNumber="+254"+formatNumber;
	        List<Recipient> response = sms.send(messageContent,com.jaguarpetroleum.JgasRegistration.Configs.Constants.ATMASK,new String[] {formatedNumber}, true);
	        logger.info("SMS sending response" +response);	    
	} 
}
