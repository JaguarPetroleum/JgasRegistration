package com.jaguarpetroleum.JgasRegistration.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.Survey;
import com.jaguarpetroleum.JgasRegistration.Repository.SurveyRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SurveyService {
	@Autowired
	private SurveyRepository surveyRepository;
	
	public void save(Survey survey) {
		surveyRepository.save(survey);
	}
	
	public List<Survey> allSurveys() {
		return surveyRepository.findAll();
	}
	
	public List<Survey> findByOrderNo(String orderNo) {
		return surveyRepository.findByOrderNo(orderNo);
	}	
}
