package com.jaguarpetroleum.JgasRegistration.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.User;
import com.jaguarpetroleum.JgasRegistration.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User findByPhoneNumber(String phoneNumber) {
		return userRepository.findByPhoneNumber(phoneNumber);
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public void updatePassword(String phoneNumber,  String encrypted_newPassword) {
		userRepository.updatePassword(phoneNumber, encrypted_newPassword);
	}
	
	public void updateRole(String phoneNumber,  String role) {
		userRepository.updateRole(phoneNumber, role);
	}
	
	public User login(String phoneNumber,  String password) {
		return userRepository.login(phoneNumber, password);
	}
}
