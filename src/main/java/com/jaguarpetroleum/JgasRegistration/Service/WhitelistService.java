package com.jaguarpetroleum.JgasRegistration.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.Whitelist;
import com.jaguarpetroleum.JgasRegistration.Repository.WhitelistRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WhitelistService {
	@Autowired
	private WhitelistRepository whitelistRepository;
	
	public Whitelist findByPhoneNumber(String phoneNumber) {
		return whitelistRepository.findByPhoneNumber(phoneNumber);
	}
}
