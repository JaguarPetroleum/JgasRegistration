package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.StaffDiscount;

@Repository
public interface StaffDiscountRepository extends JpaRepository<StaffDiscount, Integer> {
	@Query(value = "SELECT * FROM tb_StaffOffer WHERE active = 1 AND locationId = :locationId", nativeQuery = true)
	public StaffDiscount findActiveDiscount(@Param("locationId") String locationId);
	
	@Modifying 
	@Query(value = "UPDATE tb_StaffOffer SET active = 0 WHERE locationId = :locationId", nativeQuery = true)
	public void updateActive(@Param("locationId") String locationId);
}
