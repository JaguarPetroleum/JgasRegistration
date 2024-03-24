package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.Whitelist;

@Repository
public interface WhitelistRepository extends JpaRepository<Whitelist, String> {
	@Query(value = "SELECT * FROM tb_Whitelist WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	public Whitelist findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
