package com.jaguarpetroleum.JgasRegistration.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
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

import com.jaguarpetroleum.JgasRegistration.Model.OrderHD;
import com.jaguarpetroleum.JgasRegistration.Model.OrderLN;
import com.jaguarpetroleum.JgasRegistration.Model.Product;
import com.jaguarpetroleum.JgasRegistration.Model.Registration;
import com.jaguarpetroleum.JgasRegistration.Model.Ride;
import com.jaguarpetroleum.JgasRegistration.Model.StaffDiscount;
import com.jaguarpetroleum.JgasRegistration.Service.OrderHDService;
import com.jaguarpetroleum.JgasRegistration.Service.OrderLNService;
import com.jaguarpetroleum.JgasRegistration.Service.RegistrationService;
import com.jaguarpetroleum.JgasRegistration.Service.RideService;
import com.jaguarpetroleum.JgasRegistration.Service.StaffDiscountService;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class OrdersController {
	private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);
	@Autowired
	private OrderHDService orderHDService;	
	@Autowired
	private OrderLNService orderLNService;	
	@Autowired
	private com.jaguarpetroleum.JgasRegistration.Service.ProductService productService;	
	@Autowired
	private RegistrationService registrationService;	
	@Autowired
	private RideService rideService;	
	@Autowired
	private StaffDiscountService staffDiscountService;
	
	@GetMapping("/getOrder/{orderNo}")
	public OrderHD getOrder(@PathVariable String orderNo) {
		return orderHDService.findByOrderNo(orderNo);
	}
	
	@GetMapping("/getDiscount/{locationId}")
	public StaffDiscount getDiscount(@PathVariable String locationId) {
		return staffDiscountService.findActiveDiscount(locationId);
	}
	
	@PostMapping("/order")
	public JSONObject add(@RequestBody JSONObject orderDetails) {
		logger.info("Received order details "+orderDetails);
		JSONObject response = new JSONObject();
		
		OrderHD orderHD = new OrderHD();		
		try {
			orderHD.setPaymentMethod(orderDetails.getAsString("paymentMethod"));
			orderHD.setLocationId(orderDetails.getAsString("shopId"));
			orderHD.setOrderDatetime(LocalDateTime.now());
			orderHD.setOrderNo(orderDetails.getAsString("orderNo"));
			orderHD.setPhoneNumber(orderDetails.getAsString("phoneNumber"));
			orderHD.setReturnCylinder(orderDetails.getAsString("returnCylinder"));
			orderHD.setSpecificLocation(orderDetails.getAsString("specificLocation"));
			orderHD.setStatus(orderDetails.getAsString("status"));
			orderHD.setTotalCost(Double.parseDouble(orderDetails.getAsString("totalCost")));
			orderHD.setTotalProductCost(Double.parseDouble(orderDetails.getAsString("totalProductCost")));
			orderHD.setDelivery(Integer.parseInt(orderDetails.getAsString("delivery")));
			orderHD.setDeliveryCost(Double.parseDouble(orderDetails.getAsString("deliveryCost")));
			orderHD.setDestinationLatitude(Double.parseDouble(orderDetails.getAsString("destinationLatitude")));
			orderHD.setDestinationLongitude(Double.parseDouble(orderDetails.getAsString("destinationLongitude")));
			orderHD.setDistance(Double.parseDouble(orderDetails.getAsString("distance")));
			orderHD.setPickUpLatitude(Double.parseDouble(orderDetails.getAsString("pickUpLatitude")));
			orderHD.setPickUpLongitude(Double.parseDouble(orderDetails.get("pickUpLongitude").toString()));
			orderHD.setPaid(0);
			orderHD.setStatus("Order Placed");
			
			Registration regist = new Registration();
			regist = registrationService.findByPhoneNumber(orderDetails.getAsString("phoneNumber"));
			
			orderHD.setCustomerName(regist.getFirstName()+" "+regist.getLastName());
			orderHD.setCustomerNumber(regist.getPhoneNumber());
			
			orderHDService.save(orderHD);
			logger.info("Order header details have been successfully captured "+orderHD);

			
			try {
                // Parse the JSON payload
                org.json.JSONObject payloadObject = new org.json.JSONObject(orderDetails);

                JSONArray itemObjectCounter = payloadObject.getJSONArray("orderItems");
                // Iterate through the JSONArray using a for loop
                for (int i = 0; i < itemObjectCounter.length(); i++) {
                	org.json.JSONObject itemObject = payloadObject.getJSONArray("orderItems").getJSONObject(i);
                    int unitQuantity = itemObject.getInt("unitQuantity");
                    String productId = itemObject.getString("productId");
                    Double productPrice = itemObject.getDouble("productPrice");
                    
                    OrderLN orderLN = new OrderLN();
                	
        			orderLN.setOrderNo(orderDetails.getAsString("orderNo"));
        			orderLN.setOrderPrice(productPrice * unitQuantity);
        			orderLN.setProductId(productId);
        			orderLN.setUnitPrice(productPrice);
        			orderLN.setOrderQuantity(unitQuantity);	   
        			orderLN.setProductName(productService.findByProductId(productId).getProductName());
        			
        			orderLNService.save(orderLN);
        			logger.info("Order line details have been successfully captured "+orderLN);
                } 
                                
                response.put("resultCode", 0);
                response.put("resultMessage", "Successfully placed the order");
  
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error occured capturing order details. "+e.getMessage());
            }							
			
		} catch(Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "An error occured while placing order. Details - "+e.getLocalizedMessage());
		}
		logger.info("Order status "+response);
		return response;
	}
	
	@PostMapping("/scanCylinder")
	public JSONObject scanCylinder(@RequestBody JSONObject scanDetails) {
		logger.info("Received details from cylinder scan "+scanDetails);
		JSONObject response = new JSONObject();
		try {
			orderLNService.updateScanDetails(scanDetails.get("tagNo").toString(), 
					scanDetails.get("weight").toString(), 
					scanDetails.get("serialNo").toString(), 
					scanDetails.get("orderNo").toString(), 
					scanDetails.get("productId").toString());
			
			response.put("resultCode", 0);
			response.put("resultMessage", "Scan details have been successfully captured");
		} catch(Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "An error occured while scanning the cylinder. Details - "+e.getLocalizedMessage());
		}
		return response;
	}
	
	@GetMapping("/confirmPayment/{orderNo}")
	public JSONObject confirmPayment(@PathVariable String orderNo){
		JSONObject response = new JSONObject();
		try {
			OrderHD payment = new OrderHD();
			payment = orderHDService.findByOrderNo(orderNo);
			
			response.put("paid", payment.getPaid());			
			response.put("transactionRef", payment.getTransactionRef());
			response.put("paymentMethod", payment.getPaymentMethod());
			response.put("resultCode", 0);
			response.put("resultMessage", "Success");
		} catch(Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", e.getLocalizedMessage());
		}
		return response;
	}
	
	@GetMapping("/orderItems2/{orderNo}")
	public JSONArray getOrderItems(@PathVariable String orderNo){
		logger.info("Received request for Order items in order "+ orderNo);
		JSONArray jarray = new JSONArray();
		
		try {
			List<OrderLN> orderLN = orderLNService.findByOrderNo(orderNo);			 
			logger.info("Order items "+ orderLN);
			
			Product product = new Product();			
			
			for (int i = 0; i < orderLN.size(); i++) {
				JSONObject res = new JSONObject();				
				OrderLN orderItem = new OrderLN();
				orderItem = orderLN.get(i);
				
				res.put("productId", orderItem.getProductId());
				res.put("buyingPrice", orderItem.getBuyingPrice());
				res.put("orderPrice", orderItem.getOrderPrice());
				res.put("orderQuantity", orderItem.getOrderQuantity());
				res.put("orderNo", orderItem.getOrderNo());
				res.put("unitPrice", orderItem.getUnitPrice());
				res.put("refilledCylinderSerial", orderItem.getRefilledCylinderSerial());
				res.put("weight", orderItem.getWeight());
				res.put("serialNo", orderItem.getSerialNo());
				
				product = productService.findByProductId(orderItem.getProductId());
				
				res.put("productName", product.getProductName());
				logger.info("Item details "+res);
				
				jarray.put(res.toJSONString());		
			}
			return jarray;
		} catch(Exception e) {
			logger.error("Error encountered while retrieving order items "+e.getMessage());
			return null;
		}				
	}
	
	@GetMapping("/orderItems/{orderNo}")
	public List<OrderLN> getOrderItems2(@PathVariable String orderNo){
		logger.info("Received request for Order items in order "+ orderNo);
		
		try {
			return orderLNService.findByOrderNo(orderNo);
		} catch(Exception e) {
			logger.error("Error encountered while retrieving order items "+e.getMessage());
			return null;
		}				
	}
	
	@GetMapping("/allOrders")
	public List<OrderHD> getAllOrders(){
		try {
			return orderHDService.listAll();

		} catch(Exception e) {
			return null;
		}		
	}
	
	@GetMapping("/allShopOrders/{locationId}")
	public List<OrderHD> getAllShopOrders(@PathVariable String locationId){
		try {
			return orderHDService.findByLocationId(locationId);

		} catch(Exception e) {
			return null;
		}		
	}
	
	@GetMapping("/allCustomerOrders/{phoneNumber}")
	public List<OrderHD> getAllCustomerOrders(@PathVariable String phoneNumber){
		try {
			return orderHDService .findByPhoneNumber(phoneNumber);

		} catch(Exception e) {
			return null;
		}		
	}
	
	@PutMapping("/updateOrderStatus/{orderNo}/{status}")
	public JSONObject updateStatus(@PathVariable String orderNo, @PathVariable String status){
		JSONObject res = new JSONObject();
		try {
			orderHDService.updateStatus(status, orderNo);
			res.put("resultCode", 0);
			res.put("resultMessage", "Successfully updated order "+orderNo+" status to "+status);
		} catch(Exception e) {
			res.put("resultCode", 1000);
			res.put("resultMessage", "Failed to update status. Error Details: "+e.getMessage());
		}		
		return res;
	}
	
	@PutMapping("/updateOrderRecepient/{orderNo}/{recepientName}/{recepientPhone}")
	public JSONObject updateRecepient(@PathVariable String orderNo, @PathVariable String recepientName, @PathVariable String recepientPhone){
		JSONObject res = new JSONObject();
		try {
			orderHDService.updateRecepient(recepientName, recepientPhone, orderNo);
			res.put("resultCode", 0);
			res.put("resultMessage", "Successfully updated recepient details for order "+orderNo);
		} catch(Exception e) {
			res.put("resultCode", 1000);
			res.put("resultMessage", "Failed to update recepient. Error Details: "+e.getMessage());
		}		
		return res;
	}
	
	@GetMapping("/rideByOrderNumber/{orderNo}")
	public Ride rideByOrderNumber(@PathVariable String orderNo){
		try {
			return rideService.findByOrderNo(orderNo);
		} catch(Exception e) {
			return null;
		}		
	}
	

}
