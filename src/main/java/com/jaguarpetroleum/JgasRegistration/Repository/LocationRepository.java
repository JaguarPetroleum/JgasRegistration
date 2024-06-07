package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, String>{
	@Query(value = "SELECT * FROM tb_Location WHERE locationId = :locationId", nativeQuery = true)
	public Location findByLocationId(@Param("locationId") String locationId);
	
	@Query(value = "SELECT * FROM tb_Location WHERE locationName = :locationName", nativeQuery = true)
	public Location findByLocationName(@Param("locationName") String locationName);
	
	@Query(value = "SELECT * FROM tb_Location WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	public Location findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
	
	@Modifying 
	@Query(value = "UPDATE tb_Location SET deviceToken = :deviceToken WHERE locationId = :locationId", nativeQuery = true)
	public void updateDeviceToken(@Param("deviceToken") String deviceToken, @Param("locationId") String locationId);
}
