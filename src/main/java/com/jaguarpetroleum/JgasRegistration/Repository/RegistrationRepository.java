package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
	@Query(value = "SELECT * FROM tb_Registration WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	public Registration findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
	
	@Modifying 
	@Query(value = "UPDATE tb_Registration SET isStaff = :isStaff, staffNumber = :staffNumber WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	public void updateStaff(@Param("isStaff") Integer isStaff, @Param("staffNumber") String staffNumber, @Param("phoneNumber") String phoneNumber);
}
