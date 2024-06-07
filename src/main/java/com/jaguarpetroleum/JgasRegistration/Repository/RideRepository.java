package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Integer> {
	@Query(value = "SELECT * FROM tb_Ride WHERE orderNo = :orderNo AND STATUS IN ('RIDER ASSIGNED', 'ARRIVED', 'ACCEPTED', 'STARTED', 'ENDED', 'RATED', 'DELIVERED', 'OPEN', 'ENROUTE', '') ORDER BY recordNo DESC LIMIT 1", nativeQuery = true)
	public Ride findByOrderNo(@Param("orderNo") String orderNo);
	
	@Query(value = "SELECT * FROM tb_Ride WHERE tripId = :tripId AND status != 'CANCELLED'", nativeQuery = true)
	public Ride findByTripId(@Param("tripId") String tripId);
	
	@Modifying 
	@Query(value = "UPDATE tb_Ride SET startCode = :startCode, endCode = :endCode, parking = :parking WHERE tripId = :tripId", nativeQuery = true)
	public void updateCodes(@Param("startCode") String startCode, @Param("endCode") String endCode, @Param("tripId") String tripId, @Param("parking") String parking);
	
	@Modifying 
	@Query(value = "UPDATE tb_Ride SET status = :status WHERE tripId = :tripId", nativeQuery = true)
	public void updateStatus(@Param("status") String status, @Param("tripId") String tripId);
	
	@Modifying 
	@Query(value = "UPDATE tb_Ride SET deliveryCodeSent = 1 WHERE tripId = :tripId", nativeQuery = true)
	public void updateDeliveryCodeStatus(@Param("tripId") String tripId);

}
