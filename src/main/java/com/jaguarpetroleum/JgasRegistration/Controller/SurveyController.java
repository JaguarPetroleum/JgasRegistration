package com.jaguarpetroleum.JgasRegistration.Controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jaguarpetroleum.JgasRegistration.Model.Survey;
import com.jaguarpetroleum.JgasRegistration.Service.SurveyService;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class SurveyController {
	private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);
	@Autowired
	private SurveyService surveyService;	
	
	@PostMapping("/survey")
	public JSONObject survey(@RequestBody JSONObject surveyDetails) {
		logger.info("Received rating details "+surveyDetails);
		JSONObject response = new JSONObject();
		Survey survey = new Survey();
		
		try {
			survey.setCommunicationProcess(surveyDetails.get("communications").toString());
			survey.setOrderNo(surveyDetails.get("orderNo").toString());
			survey.setOrderProcess(surveyDetails.get("ordering").toString());
			survey.setPaymentProcess(surveyDetails.get("payments").toString());
			survey.setPhoneNumber(surveyDetails.get("phoneNumber").toString());
			survey.setRate(Integer.parseInt(surveyDetails.get("rate").toString()));
			survey.setResponse(surveyDetails.get("response").toString());
			survey.setPhoneNumber(surveyDetails.get("phoneNumber").toString());
			survey.setAdditionalComments(surveyDetails.get("additionalComments").toString());
			
			surveyService.save(survey);
			response.put("resultCode", 0);
			response.put("resultMessage", "Successfully captured survey details for order "+survey.getOrderNo());
			
		} catch(Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "An error occured while capturing survey. Details - "+e.getLocalizedMessage());
		}
		return response;
	}
	
	@GetMapping("/viewAllSurveys")
	public ResponseEntity<List<Survey>> getAll(){
		HttpHeaders header = new HttpHeaders();
		return ResponseEntity.status(HttpStatus.OK).headers(header).body(surveyService.allSurveys());
	}
	
	@GetMapping("/getSurveyByOrderNo/{orderNo}")
	public List<Survey> getSurveyByOrderNo(@PathVariable String orderNo){
		return surveyService.findByOrderNo(orderNo);
	}
}
