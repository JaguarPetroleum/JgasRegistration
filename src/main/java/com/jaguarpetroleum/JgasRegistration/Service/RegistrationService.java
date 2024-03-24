package com.jaguarpetroleum.JgasRegistration.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.Registration;
import com.jaguarpetroleum.JgasRegistration.Repository.RegistrationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistrationService {

	@Autowired
	private RegistrationRepository registrationRepository;
	
	public Registration findByPhoneNumber(String phoneNumber) {
		return registrationRepository.findByPhoneNumber(phoneNumber);
	}
	
	public void save(Registration registration) {
		registrationRepository.save(registration);
	}
	
	public List<Registration> allRegistrations() {
		return registrationRepository.findAll();
	}
	
	public void updateStaff(Integer isStaff, String staffNumber, String phoneNumber) {
		registrationRepository.updateStaff(isStaff, staffNumber, phoneNumber);
	}
}
