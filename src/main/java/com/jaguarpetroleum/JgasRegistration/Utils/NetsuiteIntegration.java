package com.jaguarpetroleum.JgasRegistration.Utils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetsuiteIntegration {
	private static final Logger logger = LoggerFactory.getLogger(NetsuiteIntegration.class);
	public void transferOrder(JSONObject transferOrderDetails) {
		logger.info("Transfer order request "+transferOrderDetails);
	}
	
	public void salesOrder(JSONObject salesOrderDetails) {
		logger.info("Sales order request "+salesOrderDetails);
		
	}
}
