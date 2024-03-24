package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.StaffDiscount;

@Repository
public interface StaffDiscountRepository extends JpaRepository<StaffDiscount, Integer> {
	@Query(value = "SELECT * FROM tb_StaffOffer WHERE active = 1", nativeQuery = true)
	public StaffDiscount findActiveDiscount();
	
	@Modifying 
	@Query(value = "UPDATE tb_StaffOffer SET active = 0", nativeQuery = true)
	public void updateActive();
}
