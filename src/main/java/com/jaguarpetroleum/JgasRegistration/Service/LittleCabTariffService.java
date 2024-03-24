package com.jaguarpetroleum.JgasRegistration.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.LittleCabTariff;
import com.jaguarpetroleum.JgasRegistration.Repository.LittleCabTariffRepository;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;

@Service
@Transactional
public class LittleCabTariffService {
	@Autowired
	private LittleCabTariffRepository littleCabTariffRepository;
	
	public JSONObject findByDistance(String distance) {
		return littleCabTariffRepository.findByDistance(distance);
	}
	
	public void save(LittleCabTariff littleCabTariff) {
		littleCabTariffRepository.save(littleCabTariff);
	}

}
