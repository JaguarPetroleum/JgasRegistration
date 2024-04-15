package com.jaguarpetroleum.JgasRegistration.Controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jaguarpetroleum.JgasRegistration.Model.Registration;
import com.jaguarpetroleum.JgasRegistration.Model.User;
import com.jaguarpetroleum.JgasRegistration.Service.LocationService;
import com.jaguarpetroleum.JgasRegistration.Service.RegistrationService;
import com.jaguarpetroleum.JgasRegistration.Service.UserService;
import com.jaguarpetroleum.JgasRegistration.Service.WhitelistService;
import com.jaguarpetroleum.JgasRegistration.Utils.AfricasTalkingIntegration;
import com.jaguarpetroleum.JgasRegistration.Utils.OTPGenerator;
import com.jaguarpetroleum.JgasRegistration.Utils.TrippleDes;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class RegistrationController {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	OTPGenerator otp = new OTPGenerator();
	AfricasTalkingIntegration africasTalking = new AfricasTalkingIntegration();
	
	private Registration registration = new Registration();
	private User myUser = new User();
	
	@Autowired
	private RegistrationService registrationService;	
	@Autowired
	private UserService userService;
	@Autowired
	private WhitelistService whitelistService;
	@Autowired
	private LocationService locationService;

	
	@GetMapping("/getByPhoneNumber/{phoneNumber}")
	public ResponseEntity<Registration> get(@PathVariable String phoneNumber){
		Registration registration = registrationService.findByPhoneNumber(phoneNumber);
		HttpHeaders header = new HttpHeaders();
		return ResponseEntity.status(HttpStatus.OK).headers(header).body(registration);
	}
	
	@GetMapping("/allRegistrations")
	public ResponseEntity<List<Registration>> getAll(){
		HttpHeaders header = new HttpHeaders();
		return ResponseEntity.status(HttpStatus.OK).headers(header).body(registrationService.allRegistrations());
	}
	
	@PostMapping("/register")
	public JSONObject add(@RequestBody JSONObject registrationDetails) {
		JSONObject response = new JSONObject();
		logger.info("Received request to register a user "+registrationDetails);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	long recordNo = timestamp.getTime();
		
		
		//if(whitelistService.findByPhoneNumber(registrationDetails.get("contactDefault").toString()) != null) {
			if(registrationService.findByPhoneNumber(registrationDetails.get("contactDefault").toString())==null) {
				try {
					registration.setAlternativePhone(registrationDetails.get("contactAlternative").toString());
					registration.setEmailAddress(registrationDetails.get("email").toString());
					registration.setFirstName(registrationDetails.get("firstName").toString());
					registration.setLastName(registrationDetails.get("lastName").toString());
					registration.setLatitude(registrationDetails.get("latitude").toString());
					registration.setLongitude(registrationDetails.get("longitude").toString());
					registration.setPhoneNumber(registrationDetails.get("contactDefault").toString());
					registration.setIdNumber(registrationDetails.get("nationalRegistration").toString());
					registration.setHomeAddress(registrationDetails.get("homeAddress").toString());
					registration.setRecordDatetime(LocalDateTime.now());
					registration.setRecordNo(recordNo);
					registration.setIsStaff(0);
					
					TrippleDes td = new TrippleDes();
					
					myUser.setPasscode(td.encrypt(registrationDetails.get("password").toString()));
					myUser.setPhoneNumber(registrationDetails.get("contactDefault").toString());
					myUser.setRole("Customer");
					myUser.setStatus(1);
					
					registrationService.save(registration);
					logger.info("New user has been successfully registered. Details "+registration);
					userService.save(myUser);
					
					logger.info("New user has been created "+myUser);
					
					response.put("resultCode", 0);
					response.put("resultMessage", "System user successfully created and granted system permissions");
				} catch (Exception e) {
					response.put("resultCode", 10010);
					response.put("resultMessage", "An error was encountered registering the user. Kindly try again later. "+e.getMessage());
				}	
			} else {
				response.put("resultCode", 20010);
				response.put("resultMessage", "There is a user already attached to the number "+registrationDetails.get("contactDefault").toString());
			}
		//} else {
			//response.put("resultCode", 30010);
			//response.put("resultMessage", "There was an error while registering your details. Please try again later");
		//}		
			
		logger.info("Response registering user "+response);
		return response;
	}
	
	@PostMapping("/forgotPassword")
	public JSONObject updatePassword(@RequestBody JSONObject details) {
		JSONObject response = new JSONObject();
		try {
			TrippleDes td = new TrippleDes();
			
			userService.updatePassword(details.get("defaultContact").toString() ,  td.encrypt(details.get("password").toString()));
			response.put("resultCode", 0);
			response.put("resultMessage", "Passcode has been successfully updated");
		} catch (Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "Error updating Passcode. Please try again");
		}		
		return response;
	}
	
	@PostMapping("/changeRole")
	public JSONObject changeRole(@RequestBody JSONObject changeRole) {
		JSONObject response = new JSONObject();
		try {			
			userService.updateRole(changeRole.get("phoneNumber").toString() ,changeRole.get("role").toString());
			response.put("resultCode", 0);
			response.put("resultMessage", "Role has been successfully updated to "+changeRole.get("role").toString()+" for user "+changeRole.get("phoneNumber").toString());
		} catch (Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "Error updating user role. Please try again");
		}		
		return response;
	}
	
	@PostMapping("/loginUser")
	public JSONObject loginUser(@RequestBody JSONObject loginDetails) {
		JSONObject response = new JSONObject();
		try {
			TrippleDes td = new TrippleDes();
			if(userService.login(loginDetails.get("phoneNumber").toString(), td.encrypt(loginDetails.get("password").toString())) == null) {
				response.put("resultCode", 10010);
				response.put("resultMessage", "User not authorised to access the system. Ensure you have captured the right username:password combination");
				
			} else {
				response.put("resultCode", 0);
				response.put("resultMessage", "User successfully logged in");
				response.put("token", otp.token());
				response.put("refreshToken", otp.token());
				response.put("role", userService.login(loginDetails.get("phoneNumber").toString(), td.encrypt(loginDetails.get("password").toString())).getRole());
			}			
		} catch (Exception e) {
			response.put("resultCode", 20010);
			response.put("resultMessage", "We couldnt log you in at the moment. Please try again");
		}		
		return response;
	}
	
	@PostMapping("/vendorDetails/{phoneNumber}")
	public JSONObject vendorDetails(@PathVariable String phoneNumber) {
		JSONObject response = new JSONObject();
		try {
			TrippleDes td = new TrippleDes();
			if(locationService.findByPhoneNumber(phoneNumber) == null) {
				response.put("resultCode", 10010);
				response.put("resultMessage", "User not attached to any shop.");
				
			} else {
				response.put("resultCode", 0);
				response.put("resultMessage", "Vendor successfully logged in");
				response.put("vendorNames", registrationService.findByPhoneNumber(phoneNumber).getFirstName()+" "+registrationService.findByPhoneNumber(phoneNumber).getLastName());
				response.put("locationId", locationService.findByPhoneNumber(phoneNumber).getLocationId());
				response.put("locationName", locationService.findByPhoneNumber(phoneNumber).getLocationName());
				//locationService.findByPhoneNumber(phoneNumber)
			}			
		} catch (Exception e) {
			response.put("resultCode", 20010);
			response.put("resultMessage", "An error was encountered trying to get vendor details. "+e.getMessage());
		}		
		return response;
	}
	
	@PostMapping("/generateOtp/{phoneNumber}")
	public JSONObject generateOtp(@PathVariable String phoneNumber) {
		JSONObject response = new JSONObject();
		if(registrationService.findByPhoneNumber(phoneNumber)== null) {
			try {
				String otpMessage = otp.getAlphaNumericString();
				africasTalking.sendSms(phoneNumber, "J-Gas: Your OTP is "+ otpMessage);
				response.put("resultCode", 0);
				response.put("phoneNumber", phoneNumber);
				response.put("otp", otpMessage);
				response.put("resultMessage", "Success");
			} catch(Exception e) {
				response.put("resultCode", 10010);
				response.put("phoneNumber", phoneNumber);
				response.put("resultMessage", e.getLocalizedMessage());
			}
		} else {
			response.put("resultCode", 20010);
			response.put("phoneNumber", phoneNumber);
			response.put("resultMessage", "There is an existing user attached to the number "+phoneNumber);
		}
		
		logger.info("OTP request response "+response);
		return response;
	}
	
	@PostMapping("/forgotGenerateOtp/{phoneNumber}")
	public JSONObject forgotGenerateOtp(@PathVariable String phoneNumber) {
		JSONObject response = new JSONObject();
		if(registrationService.findByPhoneNumber(phoneNumber) != null) {
			try {
				String otpMessage = otp.getAlphaNumericString();
				africasTalking.sendSms(phoneNumber, "J-Gas: Your OTP is "+otpMessage);
				response.put("resultCode", 0);
				response.put("phoneNumber", phoneNumber);
				response.put("otp", otpMessage);
				response.put("resultMessage", "Success");
			} catch(Exception e) {
				response.put("resultCode", 10010);
				response.put("phoneNumber", phoneNumber);
				response.put("resultMessage", e.getLocalizedMessage());
			}
		} else {
			response.put("resultCode", 20010);
			response.put("phoneNumber", phoneNumber);
			response.put("resultMessage", "There is no user attached to the number "+phoneNumber);
		}
		
		logger.info("OTP request response "+response);
		return response;
	}
	
	@PutMapping("/updateStaffDetails")
	public JSONObject updateStaffDetails(@RequestBody JSONObject staffDetails) {
		JSONObject response = new JSONObject();
		try {			
			registrationService.updateStaff(Integer.parseInt(staffDetails.get("isStaff").toString()) ,
					staffDetails.get("staffNumber").toString() , 
					staffDetails.get("phoneNumber").toString());
			response.put("resultCode", 0);
			response.put("resultMessage", "User has been successfully updated to staff with staff number "+staffDetails.get("staffNumber").toString());
		} catch (Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "Error updating user to staff. Please try again");
		}		
		return response;
	}
}
