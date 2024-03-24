package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	@Query(value = "SELECT * FROM tb_User WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	public User findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
	
	@Query(value = "SELECT * FROM tb_User WHERE phoneNumber = :phoneNumber and passcode = :password", nativeQuery = true)
	public User login(@Param("phoneNumber") String phoneNumber, @Param("password") String password);
	
	@Modifying 
	@Query(value = "UPDATE tb_User SET passcode = :encrypted_newPassword WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	int updatePassword(@Param("phoneNumber") String phoneNumber, @Param("encrypted_newPassword") String encrypted_newPassword);
	
	@Modifying 
	@Query(value = "UPDATE tb_User SET role = :role WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	int updateRole(@Param("phoneNumber") String phoneNumber, @Param("role") String role);
}
