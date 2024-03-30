package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.LittleCabTariff;

import net.minidev.json.JSONObject;

@Repository
public interface LittleCabTariffRepository extends JpaRepository<LittleCabTariff, Integer>{
	
	@Query(value = "SELECT * FROM tb_LittleCabTariff WHERE :distance BETWEEN floor AND ceiling", nativeQuery = true)
	public JSONObject findByDistance(@Param("distance") String distance);
	

}
